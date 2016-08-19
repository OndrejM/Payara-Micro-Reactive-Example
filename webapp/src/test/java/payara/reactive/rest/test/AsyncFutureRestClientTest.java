package payara.reactive.rest.test;

import java.util.concurrent.*;
import javax.ws.rs.client.*;
import org.junit.Test;

import javax.ws.rs.core.Response;

/**
 * Created by mertcaliskan
 */
public class AsyncFutureRestClientTest {

    @Test
    public void callAsync() {
        Thread mainThread = Thread.currentThread();
        final JaxrsResponseCallback completion = new JaxrsResponseCallback();
        JaxrsResponseCallback.get(ClientBuilder.newClient()
                .target("http://localhost:8080/JavaEEReactive/rest")
                .path("async")
                .request()
                .async())
                .thenAccept(
                        response -> {
                            System.out.println("Response code " + response.getStatus()
                                    + ", with content: " + response.readEntity(String.class));
                        }
                )
                .exceptionally(throwable -> {
                    System.out.println("Failed");
                    throwable.printStackTrace();
                    return null;
                }).thenRun(() -> {
                    System.out.println("Interrupting...");
                    mainThread.interrupt();
                });

        try {
            while (true) {
                Thread.sleep(1000);
                System.out.println("I'm alive");
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted, stopping.");
        }
    }

    private static class JaxrsResponseCallback extends CompletableFuture<Response> implements InvocationCallback<Response> {

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
}
