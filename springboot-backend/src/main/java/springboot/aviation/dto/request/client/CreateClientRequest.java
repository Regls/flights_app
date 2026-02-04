package springboot.aviation.dto.request.client;

public record CreateClientRequest(
    String cpf,
    String clientFirstName,
    String clientLastName
) {}

