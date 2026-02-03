package springboot.aviation.dto.response.summary;

import springboot.aviation.domain.flight.Flight;


public record FlightSummaryResponse(
    String flightNumber
) {
    public static FlightSummaryResponse fromDomain(Flight flight) {
        return new FlightSummaryResponse(
            flight.getFlightNumber()
        );
    }
}