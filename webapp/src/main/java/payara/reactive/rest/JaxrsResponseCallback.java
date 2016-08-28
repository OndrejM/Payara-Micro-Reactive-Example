package payara.reactive.rest;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by mertcaliskan
 */
public class JaxrsResponseCallback extends CompletableFuture<Response> implements InvocationCallback<Response> {

    @Override
    public void completed(Response response) {
        super.complete(response);
    }

    @Override
    public void failed(Throwable throwable) {
        super.completeExceptionally(throwable);
    }

    public static CompletionStage<Response> get(AsyncInvoker invoker) {
        final JaxrsResponseCallback completion = new JaxrsResponseCallback();
        invoker.get(completion);
        return completion;
    }
}