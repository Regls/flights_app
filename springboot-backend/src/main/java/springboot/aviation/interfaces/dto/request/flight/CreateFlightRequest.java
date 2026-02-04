package springboot.aviation.interfaces.dto.request.flight;

import java.time.LocalDateTime;

public record CreateFlightRequest(
    String flightNumber,
    Long airlineId,
    Long departureAirportId,
    Long arrivalAirportId,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime
) {}
