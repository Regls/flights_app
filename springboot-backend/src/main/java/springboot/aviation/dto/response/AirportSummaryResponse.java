/* package springboot.aviation.dto.response;

import springboot.aviation.model.Airport;


public class AirportSummaryResponse {
    
    public String iataCode;
    public String airportName;

    private AirportSummaryResponse() {
    }

    public static AirportSummaryResponse from(Airport airport) {
        AirportSummaryResponse response = new AirportSummaryResponse();
        response.iataCode = airport.hasIataCode();
        response.airportName = airport.hasName();
        return response;
    }
}
 */