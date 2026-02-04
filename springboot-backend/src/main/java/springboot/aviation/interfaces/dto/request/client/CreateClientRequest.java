package springboot.aviation.interfaces.dto.request.client;

public record CreateClientRequest(
    String cpf,
    String clientFirstName,
    String clientLastName
) {}

