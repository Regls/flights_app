/* package springboot.aviation.dto.response;

import springboot.aviation.model.FlightStatus;
import springboot.aviation.model.Flight;

import static springboot.aviation.dto.utils.FormatUtils.formatDateTime;


public class FlightResponse {
    
    public Long id;
    public String flightNumber;
    public AirlineSummaryResponse airline;
    public AirportSummaryResponse departureAirport;
    public AirportSummaryResponse arrivalAirport;
    public String departureTime;
    public String arrivalTime;
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
        response.departureTime = formatDateTime(flight.hasDepartureTime());
        response.arrivalTime = formatDateTime(flight.hasArrivalTime());
        response.status = flight.hasStatus();
        return response;
    }
}
 */