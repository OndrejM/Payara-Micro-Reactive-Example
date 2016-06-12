package payara.reactive.rest;

import fish.payara.micro.cdi.Inbound;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.container.AsyncResponse;
import payara.reactive.events.ComputationResponse;
import payara.reactive.util.Logging;

@ApplicationScoped
public class AsyncResponseMap {

    private final Map<Integer, AsyncResponse> map = new ConcurrentHashMap<>();
    
    public void put(int id, AsyncResponse response) {
        map.put(id, response);
    }
    
    public Optional<AsyncResponse> remove(int id) {
        return Optional.ofNullable(map.remove(id));
    }
    
    public void handleAnswer(@Observes @Inbound ComputationResponse result) {
        Logging.logMessage("Received response");
        remove(result.getRequestId())
                .ifPresent((AsyncResponse resp) -> {
                    if (result.getErrorMessage() == null) {
                        resp.resume(result.getAnswer());
                    } else {
                        resp.resume(new Exception(result.getErrorMessage()));
                    }
                });
    }

}
