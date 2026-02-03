package springboot.aviation.domain.booking;

import java.time.LocalDateTime;

import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.flight.Flight;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.BookingMessages;


public class Booking {
    
    private Long id;
    private Client client;
    private Flight flight;
    private String bookingCode;
    private LocalDateTime createdAt;
    private BookingStatus status;

    private static final String BOOKING_CODE_PATTERN = "^[A-Z0-9]{6}$";

    public Booking(Long id, Client client, Flight flight, String bookingCode, LocalDateTime createdAt, BookingStatus status) {
        this.id = id;
        this.client = client;
        this.flight = flight;
        this.bookingCode = bookingCode;
        this.createdAt = createdAt;
        this.status = status;
    }

    public static Booking create(Client client, Flight flight, String bookingCode) {
        validateCreationRules(client, flight, bookingCode);
        return new Booking(null, client, flight, bookingCode, LocalDateTime.now(), BookingStatus.CREATED);
    }

    public static Booking restore(Long id, Client client, Flight flight, String bookingCode, LocalDateTime createdAt, BookingStatus status) {
        return new Booking(id, client, flight, bookingCode, createdAt, status);
    }

    private static void validateCreationRules(Client client, Flight flight, String bookingCode) {
        if (client == null) {
            throw new BusinessException(BookingMessages.CLIENT_REQUIRED);
        }
        if (flight == null) {
            throw new BusinessException(BookingMessages.FLIGHT_REQUIRED);
        }
        if (bookingCode == null || bookingCode.isBlank()) {
            throw new BusinessException(BookingMessages.BOOKING_CODE_REQUIRED);
        }
        if (!bookingCode.matches(BOOKING_CODE_PATTERN)) {
            throw new BusinessException(BookingMessages.BOOKING_CODE_INVALID);
        }
        if (!client.isActive()) {
            throw new BusinessException(BookingMessages.CLIENT_ACTIVE);
        }
        if (!flight.isScheduled()){
            throw new BusinessException(BookingMessages.FLIGHT_SCHEDULED);
        }
    }

    public void confirm() {
        if (!isCreated()) {
            throw new BusinessException(BookingMessages.CONFIRM_ONLY_CREATED);
        }
        if (!flight.isScheduled()) {
            throw new BusinessException(BookingMessages.CONFIRM_ONLY_FLIGHT_SCHEDULED);
        }
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        if (!(isCreated() || isConfirmed())) {
            throw new BusinessException(BookingMessages.CANCEL_ONLY_CREATED_OR_CONFIRMED);
        }
        if (!flight.isScheduled()) {
            throw new BusinessException(BookingMessages.CANCEL_ONLY_FLIGHT_SCHEDULED);
        }
        this.status = BookingStatus.CANCELLED;
    }

    public void cancelDueFlightCancel() {
        if (this.status == BookingStatus.CANCELLED) return;
        
        this.status = BookingStatus.CANCELLED;
    }

    public Long getId() { return id; }
    public Client getClient() { return client; }
    public Flight getFlight() { return flight; }
    public String getBookingCode() { return bookingCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BookingStatus getStatus() { return status; }
    public boolean isCreated() {return this.status == BookingStatus.CREATED;}
    public boolean isConfirmed() {return this.status == BookingStatus.CONFIRMED;}
    public boolean isCancelled() {return this.status == BookingStatus.CANCELLED;}
}
