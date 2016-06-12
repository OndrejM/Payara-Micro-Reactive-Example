package payara.reactive.events;

import java.io.Serializable;

public class ComputationResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int requestId;
    
    private String answer;
    
    private String errorMessage;

    public ComputationResponse(ComputationRequest request, String answer) {
        requestId = request.getId();
        this.answer = answer;
    }
    
    public static ComputationResponse error(ComputationRequest request, Throwable t) {
        ComputationResponse response = new ComputationResponse();
        response.errorMessage = t.getMessage();
        if (response.errorMessage == null) {
            response.errorMessage = "No error details";
        }
        response.errorMessage = t.getClass().getName() + ": " + response.errorMessage;
        response.requestId = request.getId();
        return response;
    }

    private ComputationResponse() {
    }

    public int getRequestId() {
        return requestId;
    }

    public String getAnswer() {
        return answer;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
    
    
}
