package payara.reactive.rest.test;

import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;
import java.util.concurrent.Future;

/**
 * Created by mertcaliskan
 */
public class AsyncFutureRestClientTest {

    @Test
    public void callAsync() {
        ClientBuilder.newClient()
                .target("http://localhost:8080/JavaEEReactive/rest")
                .path("async")
                .request()
                .async()
                .get(new InvocationCallback<Response>() {
                         @Override
                         public void completed(Response response) {
                             System.out.println("Response code " + response.getStatus() +
                                     ", with content: " + response.readEntity(String.class));
                         }

                         @Override
                         public void failed(Throwable throwable) {
                             System.out.println("Failed");
                             throwable.printStackTrace();
                         }
                     }
                );

        while(true) {
            try {
                Thread.sleep(1000);
                System.out.println("I'm alive");
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
