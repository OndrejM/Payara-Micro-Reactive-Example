package payara.reactive.events;

import java.io.Serializable;

public class ComputationRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final int id;

    public ComputationRequest() {
        id = ComputationRequest.this.hashCode();
    }

    public int getId() {
        return id;
    }
    
}
