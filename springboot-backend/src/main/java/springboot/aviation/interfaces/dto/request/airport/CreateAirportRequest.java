package springboot.aviation.interfaces.dto.request.airport;

public record CreateAirportRequest(
    String iataCode,
    String airportName,
    String city
) {}
