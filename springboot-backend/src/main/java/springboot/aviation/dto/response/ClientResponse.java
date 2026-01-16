package springboot.aviation.dto.response;

import springboot.aviation.model.Client;


public class ClientResponse {

    public Long id;
    public String cpf;
    public String firstName;
    public String lastName;
    public boolean status;

    private ClientResponse(){        
    }

    public static ClientResponse from(Client client) {
        ClientResponse response = new ClientResponse();
        response.id = client.hasId();
        response.cpf = client.hasCpf();
        response.firstName = client.hasFirstName();
        response.lastName = client.hasLastName();
        response.status = client.isActive();
        return response;
    }
}
