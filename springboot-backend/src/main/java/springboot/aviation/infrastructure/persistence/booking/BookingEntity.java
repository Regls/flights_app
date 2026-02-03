package springboot.aviation.infrastructure.persistence.booking;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import springboot.aviation.domain.booking.BookingStatus;
import springboot.aviation.infrastructure.persistence.client.ClientEntity;
import springboot.aviation.infrastructure.persistence.flight.FlightEntity;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "bookings", uniqueConstraints = @UniqueConstraint(columnNames = "booking_code"))
public class BookingEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private FlightEntity flight;

    @Column(name = "booking_code", nullable = false, unique = true)
    private String bookingCode;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    public BookingEntity() {}

    public Long getId() { return id; }
    public ClientEntity getclient() { return client; }
    public FlightEntity getFlight() { return flight; }
    public String getBookingCode() { return bookingCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BookingStatus getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setClient(ClientEntity client) { this.client = client; }
    public void setFlight(FlightEntity flight) { this.flight = flight; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
