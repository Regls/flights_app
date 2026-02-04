package springboot.aviation.dto.response.airport;

import springboot.aviation.domain.airport.Airport;


public record AirportResponse(
    Long id,
    String iataCode,
    String airportName,
    String city,
    String status
){
    public static AirportResponse fromDomain(Airport airport) {
        return new AirportResponse(
        airport.getId(),
        airport.getIataCode(),
        airport.getAirportName(),
        airport.getCity(),
        airport.getStatus().name()
        );
    }
}
