package payara.reactive.rest.busfeatures;

import fish.payara.micro.cdi.Inbound;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import payara.reactive.events.features.Message;
import payara.reactive.util.Logging;

/**
 *
 * @author Ondrej Mihalyi
 */
@Dependent
public class Observer {
    public void logMessage(@Observes @Inbound Message msg) {
        Logging.logMessage("Message received: " + msg.getText());
    }
}
