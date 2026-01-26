package springboot.aviation.dto.request;

public record CreateAirportRequest(
    String iataCode,
    String airportName,
    String city
) {}
