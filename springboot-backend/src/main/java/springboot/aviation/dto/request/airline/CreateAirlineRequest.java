package springboot.aviation.dto.request.airline;

public record CreateAirlineRequest(
    String iataCode,
    String airlineName
) {}
