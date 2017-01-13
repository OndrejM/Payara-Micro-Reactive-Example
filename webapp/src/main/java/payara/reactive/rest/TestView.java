package payara.reactive.rest;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.ClientBuilder;
import java.io.Serializable;
import javax.faces.view.ViewScoped;

/**
 * Created by mertcaliskan
 */
@Named
@ViewScoped
public class TestView implements Serializable {

    public void invoke() {
        Thread mainThread = Thread.currentThread();

        HttpServletRequest request = (HttpServletRequest)
                FacesContext.getCurrentInstance().getExternalContext().getRequest();

        JaxrsResponseCallback.get(ClientBuilder.newClient()
                .target(request.getScheme() + "://" +
                        request.getServerName() + ":" +
                        request.getServerPort() +
                        request.getContextPath() + "/rest")
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
    }
}
