package springboot.aviation.dto.response;

import springboot.aviation.domain.client.Client;

import static springboot.aviation.dto.utils.FormatUtils.formatCpf;


public record ClientResponse(
    Long id,
    String cpf,
    String firstName,
    String lastName,
    boolean status
){
    public static ClientResponse fromDomain(Client client) {
        return new ClientResponse(
        client.getId(),
        formatCpf(client.getCpf()),
        client.getFirstName(),
        client.getLastName(),
        client.isActive()
        );
    }
}
