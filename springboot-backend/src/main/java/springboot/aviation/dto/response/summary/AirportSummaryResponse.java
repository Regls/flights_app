package springboot.aviation.dto.response.summary;

import springboot.aviation.domain.airport.Airport;


public record AirportSummaryResponse(
    String iataCode,
    String airportName
) {
    public static AirportSummaryResponse fromDomain(Airport airport) {
        return new AirportSummaryResponse(
            airport.getIataCode(),
            airport.getAirportName()
        );
    }
}
