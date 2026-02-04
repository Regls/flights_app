package springboot.aviation.dto.response.airline;

import springboot.aviation.domain.airline.Airline;


public record AirlineResponse(
    Long id,
    String iataCode,
    String airlineName,
    String status
) {
    public static AirlineResponse fromDomain(Airline airline) {
        return new AirlineResponse(
        airline.getId(),
        airline.getIataCode(),
        airline.getAirlineName(),
        airline.getStatus().name()
        );
    }
}
