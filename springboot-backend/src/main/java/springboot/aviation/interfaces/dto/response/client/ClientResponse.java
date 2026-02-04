package springboot.aviation.interfaces.dto.response.client;

import static springboot.aviation.interfaces.dto.utils.FormatUtils.formatCpf;

import springboot.aviation.domain.client.Client;


public record ClientResponse(
    Long id,
    String cpf,
    String firstName,
    String lastName,
    String status
){
    public static ClientResponse fromDomain(Client client) {
        return new ClientResponse(
        client.getId(),
        formatCpf(client.getCpf()),
        client.getFirstName(),
        client.getLastName(),
        client.getStatus().name()
        );
    }
}
