package springboot.aviation.dto.response.client;

import springboot.aviation.domain.client.Client;

import static springboot.aviation.dto.utils.FormatUtils.formatCpf;


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
