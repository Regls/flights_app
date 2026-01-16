package springboot.aviation.dto.response;

import springboot.aviation.model.Airline;


public class AirlineResponse {
    
    public Long id;
    public String iataCode;
    public String airlineName;
    public boolean status;

    private AirlineResponse(){
    }

    public static AirlineResponse from(Airline airline) {
        AirlineResponse response = new AirlineResponse();
        response.id = airline.hasId();
        response.iataCode = airline.hasIataCode();
        response.airlineName = airline.hasName();
        response.status = airline.isActive();
        return response;
    }
}
