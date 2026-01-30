package springboot.aviation.dto.request;

public record CreateAirlineRequest(
    String iataCode,
    String airlineName
) {}
