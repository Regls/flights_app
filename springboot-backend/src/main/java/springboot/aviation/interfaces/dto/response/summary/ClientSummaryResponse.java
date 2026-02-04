package springboot.aviation.interfaces.dto.response.summary;

import static springboot.aviation.interfaces.dto.utils.FormatUtils.formatCpf;

import springboot.aviation.domain.client.Client;


public record ClientSummaryResponse(
    String cpf,
    String firstName
) {
    public static ClientSummaryResponse fromDomain(Client client) {
        return new ClientSummaryResponse(
            formatCpf(client.getCpf()),
            client.getFirstName()
        );
    }
}
