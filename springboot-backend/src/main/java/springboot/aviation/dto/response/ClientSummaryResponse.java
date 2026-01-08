package springboot.aviation.dto.response;

import springboot.aviation.model.Client;


public class ClientSummaryResponse {
    
    public String cpf;
    public String firstName;

    private ClientSummaryResponse() {
    }

    public static ClientSummaryResponse from(Client client) {
        ClientSummaryResponse response = new ClientSummaryResponse();
        response.cpf = client.hasCpf();
        response.firstName = client.hasFirstName();
        return response;
    }
}
