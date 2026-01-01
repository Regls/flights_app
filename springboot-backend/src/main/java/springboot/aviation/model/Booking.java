package springboot.aviation.model;

import jakarta.persistence.*;
import springboot.aviation.exception.BusinessException;

import java.time.LocalDateTime;

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
            throw new BusinessException("Client cannot be null");
        }
        if (!client.isActive()) {
            throw new BusinessException("Client must be active to make a booking");
        }
        if (flight == null) {
            throw new BusinessException("Flight cannot be null");
        }
        if (!flight.isScheduled()){
            throw new BusinessException("Can only book flights that are scheduled");
        }
    }

    public void confirm() {
        if (!isCreated()) {
            throw new BusinessException("Only created bookings can be confirmed");
        }
        if (!flight.isScheduled()) {
            throw new BusinessException("Cannot confirm booking for a flight that is not scheduled");
        }
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        if (!(isCreated() || isConfirmed())) {
            throw new BusinessException("Only created or confirmed bookings can be cancelled");
        }
        if (!flight.isScheduled()) {
            throw new BusinessException("Cannot cancel booking for a flight that is not scheduled");
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
