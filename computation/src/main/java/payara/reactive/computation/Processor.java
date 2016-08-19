package payara.reactive.computation;

import fish.payara.micro.cdi.Inbound;
import fish.payara.micro.cdi.Outbound;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.*;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import payara.reactive.events.ComputationRequest;
import payara.reactive.events.ComputationResponse;
import payara.reactive.util.Logging;

@Dependent
public class Processor {
    @Inject
    @Outbound
    private Event<ComputationResponse> sendResult;
    
    public void inboundComputation(@Observes @Inbound ComputationRequest request) {
        try {
            final Cache cache = getCache();
            Logging.logMessage("Received computation request...");
            if (cache.remove(request.getId())) {
                sendResult.fire(new ComputationResponse(request, getAnswer()));
            } else {
                Logging.logMessage("Ignoring computation request, somebody else is computing...");
            }
        } catch (Throwable t) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, t.getMessage(), t);
            sendResult.fire(ComputationResponse.error(request, t));
        }
        try {
            throw new Exception();
        } catch (Exception ex) {
            
        }
    }

    private static Cache<Integer, Object> getCache() {
        return Caching.getCachingProvider()
                .getCacheManager()
                .getCache(Application.CACHE_NAME);
    }
    
    private String getAnswer() {
        try {
            Logging.logMessage("Started long computation...");
            Thread.sleep(5000);
            Logging.logMessage("...computation finished");
            return "42";
        } catch (InterruptedException ex) {
            return "Could not get answer.";
        }
    }
}
