package springboot.aviation.dto.response;

import java.time.LocalDateTime;

import springboot.aviation.model.FlightStatus;
import springboot.aviation.model.Flight;


public class FlightResponse {
    
    public Long id;
    public String flightNumber;
    public AirlineSummaryResponse airline;
    public AirportSummaryResponse departureAirport;
    public AirportSummaryResponse arrivalAirport;
    public LocalDateTime departureTime;
    public LocalDateTime arrivalTime;
    public FlightStatus status;

    private FlightResponse() {
    }

    public static FlightResponse from(Flight flight) {
        FlightResponse response = new FlightResponse();
        response.id = flight.hasId();
        response.flightNumber = flight.hasFlightNumber();
        response.airline = AirlineSummaryResponse.from(flight.hasAirline());
        response.departureAirport = AirportSummaryResponse.from(flight.hasDepartureAirport());
        response.arrivalAirport = AirportSummaryResponse.from(flight.hasArrivalAirport());
        response.departureTime = flight.hasDepartureTime();
        response.arrivalTime = flight.hasArrivalTime();
        response.status = flight.hasStatus();
        return response;
    }
}
