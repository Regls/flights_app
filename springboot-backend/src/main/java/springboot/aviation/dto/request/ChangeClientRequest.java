package springboot.aviation.dto.request;

public record ChangeClientRequest(
    String clientFirstName,
    String clientLastName
) {}