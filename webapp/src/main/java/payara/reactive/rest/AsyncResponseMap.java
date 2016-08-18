package payara.reactive.rest;

import fish.payara.micro.cdi.Inbound;
import java.util.*;
import java.util.concurrent.*;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import payara.reactive.events.ComputationResponse;
import payara.reactive.util.Logging;

@ApplicationScoped
public class AsyncResponseMap {

    private final Map<Integer, CompletableFuture> map = new ConcurrentHashMap<>();
    
    public void put(int id, CompletableFuture response) {
        map.put(id, response);
    }
    
    public Optional<CompletableFuture> remove(int id) {
        return Optional.ofNullable(map.remove(id));
    }
    
    public void handleAnswer(@Observes @Inbound ComputationResponse result) {
        Logging.logMessage("Received response");
        remove(result.getRequestId())
                .ifPresent((CompletableFuture resp) -> {
                    if (result.getErrorMessage() == null) {
                        resp.complete(result.getAnswer());
                    } else {
                        resp.completeExceptionally(new Exception(result.getErrorMessage()));
                    }
                });
    }

}
