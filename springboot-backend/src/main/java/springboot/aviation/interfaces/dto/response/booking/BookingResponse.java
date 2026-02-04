package springboot.aviation.interfaces.dto.response.booking;

import static springboot.aviation.interfaces.dto.utils.FormatUtils.formatDateTime;

import springboot.aviation.domain.booking.Booking;
import springboot.aviation.interfaces.dto.response.summary.ClientSummaryResponse;
import springboot.aviation.interfaces.dto.response.summary.FlightSummaryResponse;


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