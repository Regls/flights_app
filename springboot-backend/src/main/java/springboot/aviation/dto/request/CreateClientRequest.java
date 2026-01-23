package springboot.aviation.dto.request;

public record CreateClientRequest(
    String cpf,
    String clientFirstName,
    String clientLastName
) {}

