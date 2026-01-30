package springboot.aviation.dto.response.summary;

import springboot.aviation.domain.airline.Airline;


public record AirlineSummaryResponse(
    String iataCode,
    String airlineName
) {
    public static AirlineSummaryResponse fromDomain(Airline airline) {
        return new AirlineSummaryResponse(
            airline.getIataCode(),
            airline.getAirlineName()
        );
    }
}
