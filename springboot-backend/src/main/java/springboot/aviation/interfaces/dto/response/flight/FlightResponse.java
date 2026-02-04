package springboot.aviation.interfaces.dto.response.flight;

import static springboot.aviation.interfaces.dto.utils.FormatUtils.formatDateTime;

import springboot.aviation.domain.flight.Flight;
import springboot.aviation.interfaces.dto.response.summary.AirlineSummaryResponse;
import springboot.aviation.interfaces.dto.response.summary.AirportSummaryResponse;


public record FlightResponse(
    Long id,
    String flightNumber,
    AirlineSummaryResponse airline,
    AirportSummaryResponse departureAirport,
    AirportSummaryResponse arrivalAirport,
    String departureTime,
    String arrivalTime,
    String status
) {
    public static FlightResponse fromDomain(Flight flight) {
        return new FlightResponse(
            flight.getId(),
            flight.getFlightNumber(),
            AirlineSummaryResponse.fromDomain(flight.getAirline()),
            AirportSummaryResponse.fromDomain(flight.getDepartureAirport()),
            AirportSummaryResponse.fromDomain(flight.getArrivalAirport()),
            formatDateTime(flight.getDepartureTime()),
            formatDateTime(flight.getArrivalTime()),
            flight.getStatus().name()
        );
    }
}
