package payara.reactive.events;

import java.io.Serializable;

public class ComputationRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final int id;
    private String instanceId;

    public ComputationRequest() {
        id = ComputationRequest.this.hashCode();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    
    public int getId() {
        return id;
    }
    
}
