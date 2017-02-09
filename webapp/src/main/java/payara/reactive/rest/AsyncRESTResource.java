package payara.reactive.rest;

import fish.payara.micro.PayaraMicro;
import fish.payara.micro.cdi.Outbound;
import payara.reactive.events.ComputationRequest;
import payara.reactive.util.Logging;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("async")
@RequestScoped
public class AsyncRESTResource {

    @Inject
    @Outbound(eventName = "computation", loopBack = true)
    private Event<ComputationRequest> computation;

    @Inject
    private AsyncResponseMap asyncResponses;
    
    /**
     * The entrypoint for the demo. Requests computation via an outbound event and hooks callbacks to the appropriate CF future.
     * The requested computation is processed in external "computation" service, which again fires an outbound event with the result. If multiple computation services available, only one of them receives the event, due to synchronization using JCache locks. Enables to load-balance incoming requests for computation dynamically.
     * @param restResponse
     */
    @GET
    public void whatIsTheAnswer(@Suspended AsyncResponse restResponse) {
        Logging.logMessage("REST resource handler started, triggering computation");
        
        compute()
            .thenAccept(r -> restResponse.resume(r))
            .exceptionally(e -> restResponse.resume(e));

        Logging.logMessage("REST resource handler finished, waiting for computation");
    }

    private CompletionStage compute() {
        CompletableFuture future = new CompletableFuture();
        final ComputationRequest computationRequest = new ComputationRequest();
        String instanceName = null;
        try {
            instanceName = PayaraMicro.getInstance(false).getRuntime().getInstanceName();
        } catch (Exception e) {
            Logging.logMessage("Not running in PayaraMicro environment, instanceName will be empty");
        }
        
        computationRequest.setInstanceId(instanceName);
        
        asyncResponses.put(computationRequest.getId(), future);
        callComputationService(computationRequest);
        return future;
    }

    private void callComputationService(final ComputationRequest computationRequest) {
        getCache().put(computationRequest.getId(), "");
        computation.fire(computationRequest);
    }

    private static Cache<Integer, Object> getCache() {
        return Caching.getCachingProvider()
                .getCacheManager()
                .getCache(Application.CACHE_NAME);
    }
    
}
