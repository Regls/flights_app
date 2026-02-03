package springboot.aviation.dto.response.summary;

import springboot.aviation.domain.client.Client;

import static springboot.aviation.dto.utils.FormatUtils.formatCpf;


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
