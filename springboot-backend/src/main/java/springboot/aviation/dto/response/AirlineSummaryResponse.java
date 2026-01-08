package springboot.aviation.dto.response;

import springboot.aviation.model.Airline;


public class AirlineSummaryResponse {

    public String iataCode;
    public String airlineName;

    private AirlineSummaryResponse() {
    }

    public static AirlineSummaryResponse from(Airline airline) {
        AirlineSummaryResponse response = new AirlineSummaryResponse();
        response.iataCode = airline.hasIataCode();
        response.airlineName = airline.hasName();
        return response;
    }
    
}
