package springboot.aviation.dto.request;

import java.time.LocalDateTime;

public class CreateBookingRequest {
    public Long clientId;
    public Long flightId;
    public LocalDateTime createdAt;
}
