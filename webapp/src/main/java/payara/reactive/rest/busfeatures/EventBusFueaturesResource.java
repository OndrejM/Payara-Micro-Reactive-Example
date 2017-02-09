package payara.reactive.rest.busfeatures;

import fish.payara.micro.cdi.Outbound;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.*;
import payara.reactive.events.features.Message;
import payara.reactive.util.Logging;

/**
 *
 * @author Ondrej Mihalyi
 */
@Path("bus")
@RequestScoped
public class EventBusFueaturesResource {
    
    @Inject
    @Outbound(loopBack = true)
    Event<Message> msg;
    
    /**
     * To test: run this web app on multiple clustered instances and access this REST resource on one of them. All instances should log the message.
     */
    @GET
    public void testLoopback() {
        Logging.logMessage("Broadcasting to all...");
        msg.fire(new Message("Broadcast to all, including self"));
    }
}
