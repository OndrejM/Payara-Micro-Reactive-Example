package payara.reactive.computation;

import fish.payara.micro.cdi.Inbound;
import fish.payara.micro.cdi.Outbound;
import payara.reactive.events.ComputationRequest;
import payara.reactive.events.ComputationResponse;
import payara.reactive.util.Logging;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.cache.CacheManager;

@Dependent
public class Processor {
    @Inject
    @Outbound
    private Event<ComputationResponse> sendResult;
    
    @Inject
    CacheManager cm;

    public void inboundComputation(@Observes @Inbound ComputationRequest request) {
        try {
            final Cache cache = getCache();
            Logging.logMessage("Received computation request...");
            if (cache.remove(request.getId())) {

                startComputing(request);

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

    public void startComputing(ComputationRequest request) {
        Stream.of("The", "answer", "is", "...", "42")
                .forEach(v -> {
                    final String answer = getAnswer(v);
                    sendResult.fire(new ComputationResponse(request, answer, false));
                });
        sendResult.fire(new ComputationResponse(request, "!", true));        
    }

    private String getAnswer(String v) {
        try {
            Logging.logMessage("Started long computation...");
            Thread.sleep(2000);
            Logging.logMessage("...computation finished");
            return v;
        } catch (InterruptedException ex) {
            return "Could not get answer.";
        }
    }

    private  Cache<Integer, Object> getCache() {
        return cm.getCache(Application.CACHE_NAME);
    }
    
}
