package springboot.aviation.dto.response.booking;

import springboot.aviation.domain.booking.Booking;
import springboot.aviation.dto.response.summary.ClientSummaryResponse;
import springboot.aviation.dto.response.summary.FlightSummaryResponse;

import static springboot.aviation.dto.utils.FormatUtils.formatDateTime;


public record BookingResponse(
    Long id,
    ClientSummaryResponse client,
    FlightSummaryResponse flight,
    String bookingCode,
    String createdAt,
    String status
) {
    public static BookingResponse fromDomain(Booking booking) {
        return new BookingResponse(
            booking.getId(),
            ClientSummaryResponse.fromDomain(booking.getClient()),
            FlightSummaryResponse.fromDomain(booking.getFlight()),
            booking.getBookingCode(),
            formatDateTime(booking.getCreatedAt()),
            booking.getStatus().name()
        );
    }
}