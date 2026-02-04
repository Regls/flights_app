package springboot.aviation.interfaces.dto.request.booking;


public record CreateBookingRequest(
    Long clientId,
    Long flightId
) {}
