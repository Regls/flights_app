package springboot.aviation.dto.response;

import springboot.aviation.model.BookingStatus;
import springboot.aviation.model.Booking;

import static springboot.aviation.dto.utils.FormatUtils.formatDateTime;


public class BookingResponse {
    
    public Long id;
    public ClientSummaryResponse client;
    public FlightSummaryResponse flight;
    public String createdAt;
    public BookingStatus status;

    private BookingResponse() {
    }

    public static BookingResponse from(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.id = booking.hasId();
        response.client = ClientSummaryResponse.from(booking.hasClient());
        response.flight = FlightSummaryResponse.from(booking.hasFlight());
        response.createdAt = formatDateTime(booking.hasCreatedAt());
        response.status = booking.hasStatus();
        return response;
    }
}
