package payara.reactive.rest;

import fish.payara.micro.cdi.Outbound;
import javax.cache.Cache;
import javax.cache.Caching;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import payara.reactive.events.ComputationRequest;
import payara.reactive.util.Logging;

@Path("async")
@RequestScoped
public class AsyncRESTResource {

    @Inject
    @Outbound
    private Event<ComputationRequest> computation;

    @Inject
    private AsyncResponseMap asyncResponses;
    
    @GET
    public void whatIsTheAnswer(@Suspended AsyncResponse restResponse) {
        Logging.logMessage("REST resource handler started, triggering computation");
        final ComputationRequest computationRequest = new ComputationRequest();

        asyncResponses.put(computationRequest.getId(), restResponse);
        getCache().put(computationRequest.getId(), "");
        computation.fire(computationRequest);

        Logging.logMessage("REST resource handler finished, waiting for computation");
    }

    private static Cache<Integer, Object> getCache() {
        return Caching.getCachingProvider()
                .getCacheManager()
                .getCache(Application.CACHE_NAME);
    }
    
}
