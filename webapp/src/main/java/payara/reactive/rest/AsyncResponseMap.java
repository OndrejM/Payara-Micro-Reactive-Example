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

    public void handleAnswer(@Observes @Inbound ComputationResponse result) {
        Logging.logMessage("Received response");
        getResponseData(result.getRequestId())
            .ifPresent((ResponseContainer resp) -> {
                if (result.getErrorMessage() == null) {
                    if (!resp.result.isEmpty()) {
                        resp.result += " ";
                    }
                    resp.result += result.getAnswer();
                    if (result.isResponseComplete()) {
                        remove(result.getRequestId());
                        resp.future.complete(resp.result);
                    }
                } else {
                    resp.future.completeExceptionally(new Exception(result.getErrorMessage()));
                }
            });
    }

    private final Map<Integer, ResponseContainer> map = new ConcurrentHashMap<>();
    
    public void put(int id, CompletableFuture<String> response) {
        ResponseContainer responseContainer = new ResponseContainer();
        responseContainer.future = response;
        map.put(id, responseContainer);
    }
    
    public Optional<ResponseContainer> remove(int id) {
        return Optional.ofNullable(map.remove(id));
    }
    
    public Optional<ResponseContainer> getResponseData(int id) {
        return Optional.ofNullable(map.get(id));
    }
    
    private static class ResponseContainer {
        private CompletableFuture<String> future;
        private String result = "";
    }
    
}
