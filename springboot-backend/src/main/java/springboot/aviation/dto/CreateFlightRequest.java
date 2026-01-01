package springboot.aviation.dto;

import java.time.LocalDateTime;

public class CreateFlightRequest {
    public String flightNumber;
    public Long airlineId;
    public Long departureAirportId;
    public Long arrivalAirportId;
    public LocalDateTime departureTime;
    public LocalDateTime arrivalTime;
}
