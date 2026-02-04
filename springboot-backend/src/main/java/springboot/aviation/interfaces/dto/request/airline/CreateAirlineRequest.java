package springboot.aviation.interfaces.dto.request.airline;

public record CreateAirlineRequest(
    String iataCode,
    String airlineName
) {}
