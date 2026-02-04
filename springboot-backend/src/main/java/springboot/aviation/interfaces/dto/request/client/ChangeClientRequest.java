package springboot.aviation.interfaces.dto.request.client;

public record ChangeClientRequest(
    String clientFirstName,
    String clientLastName
) {}