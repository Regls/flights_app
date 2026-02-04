package springboot.aviation.dto.request.client;

public record ChangeClientRequest(
    String clientFirstName,
    String clientLastName
) {}