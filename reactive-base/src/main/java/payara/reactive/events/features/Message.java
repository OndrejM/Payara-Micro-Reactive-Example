package payara.reactive.events.features;

import java.io.Serializable;

/**
 *
 * @author Ondrej Mihalyi
 */
public class Message implements Serializable {
    private final String text;

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
