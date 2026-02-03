package springboot.aviation.dto.request;


public record CreateBookingRequest(
    Long clientId,
    Long flightId
) {}
