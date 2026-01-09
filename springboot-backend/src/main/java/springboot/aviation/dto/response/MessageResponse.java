package springboot.aviation.dto.response;

public class MessageResponse {
    
    public String message;

    private MessageResponse() {
    }

    public static MessageResponse of(String message) {
        MessageResponse response = new MessageResponse();
        response.message = message;
        return response;
    }
}
