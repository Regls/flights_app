/* package springboot.aviation.dto.response;

import springboot.aviation.model.Flight;

public class FlightSummaryResponse {
    
    public String flightNumber;

    private FlightSummaryResponse() {
    }

    public static FlightSummaryResponse from(Flight flight) {
        FlightSummaryResponse response = new FlightSummaryResponse();
        response.flightNumber = flight.hasFlightNumber();
        return response;
    }
}
 */