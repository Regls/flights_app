package springboot.aviation.dto.response;

import springboot.aviation.model.Airport;


public class AirportResponse {

    public Long id;
    public String iataCode;
    public String airportName;
    public String city;
    public boolean operational;

    private AirportResponse() {
    }

    public static AirportResponse from(Airport airport) {
        AirportResponse response = new AirportResponse();
        response.id = airport.hasId();
        response.iataCode = airport.hasIataCode();
        response.airportName = airport.hasName();
        response.city = airport.hasCity();
        response.operational = airport.isOperational();
        return response;
    }
}
