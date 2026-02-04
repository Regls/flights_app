package springboot.aviation.dto.request.booking;


public record CreateBookingRequest(
    Long clientId,
    Long flightId
) {}
