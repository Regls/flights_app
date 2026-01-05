package springboot.aviation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.BookingMessages;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    protected Booking() {
    }

    private Booking(Client client, Flight flight) {

        validateCreationRules(client, flight);

        this.client = client;
        this.flight = flight;
        this.createdAt = LocalDateTime.now();
        this.status = BookingStatus.CREATED;
    }

    //domain
    public static Booking createBooking(Client client, Flight flight) {
        return new Booking(client, flight);
    }

    private void validateCreationRules(Client client, Flight flight) {
        if (client == null) {
            throw new BusinessException(BookingMessages.CLIENT_REQUIRED);
        }
        if (flight == null) {
            throw new BusinessException(BookingMessages.FLIGHT_REQUIRED);
        }
        if (!client.isActive()) {
            throw new BusinessException(BookingMessages.CLIENT_ACTIVE);
        }
        if (!flight.isScheduled()){
            throw new BusinessException(BookingMessages.FLIGHT_SCHEDULED);
        }
    }

    public boolean hasClient(String cpf){
        return this.client.hasCpf(cpf);
    }
    public boolean hasFlight(String flightNumber) {
        return this.flight.hasFlightNumber(flightNumber);
    }

    private void validateNotCancelled(){
        if (isCancelled()) {
            throw new BusinessException(BookingMessages.CANCELLED_CANNOT_CHANGE);
        }
    }

    public void confirm() {
        validateNotCancelled();

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

    public boolean isCreated() {
        return this.status == BookingStatus.CREATED;
    }

    public boolean isConfirmed() {
        return this.status == BookingStatus.CONFIRMED;
    }

    public boolean isCancelled() {
        return this.status == BookingStatus.CANCELLED;
    }
}
