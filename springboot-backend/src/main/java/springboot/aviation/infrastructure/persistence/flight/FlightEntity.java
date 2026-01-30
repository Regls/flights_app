package springboot.aviation.infrastructure.persistence.flight;

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
import springboot.aviation.domain.flight.FlightStatus;
import springboot.aviation.infrastructure.persistence.airline.AirlineEntity;
import springboot.aviation.infrastructure.persistence.airport.AirportEntity;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "flights", uniqueConstraints = @UniqueConstraint(columnNames = "flight_number"))
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false, unique = true)
    private String flightNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private AirlineEntity airline;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private AirportEntity departureAirport;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private AirportEntity arrivalAirport;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status;
    
    public FlightEntity() {}

    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public AirlineEntity getAirline() { return airline; }
    public AirportEntity getDepartureAirport() { return departureAirport; }
    public AirportEntity getArrivalAirport() { return arrivalAirport; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public FlightStatus getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public void setAirline(AirlineEntity airline) { this.airline = airline; }
    public void setDepartureAirport(AirportEntity departureAirport) { this.departureAirport = departureAirport; }
    public void setArrivalAirport(AirportEntity arrivalAirport) { this.arrivalAirport = arrivalAirport; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setStatus(FlightStatus status) { this.status = status; }
}
