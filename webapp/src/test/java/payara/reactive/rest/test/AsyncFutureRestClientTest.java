package payara.reactive.rest.test;

import java.util.concurrent.*;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;

/**
 * Created by mertcaliskan
 */
public class AsyncFutureRestClientTest {

    @Test
    public void callAsync() {
        final JaxrsResponseCallback completion = new JaxrsResponseCallback();
        ClientBuilder.newClient()
                .target("http://localhost:8080/JavaEEReactive/rest")
                .path("async")
                .request()
                .async()
                .get(completion);
        completion.thenAccept(
                response -> {
                    System.out.println("Response code " + response.getStatus()
                            + ", with content: " + response.readEntity(String.class));
                }
            )
            .exceptionally(throwable -> {
                System.out.println("Failed");
                throwable.printStackTrace();
                return null;
            });

        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println("I'm alive");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
    }
}
