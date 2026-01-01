package springboot.aviation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import springboot.aviation.exception.BusinessException;


@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @ManyToOne(optional = false)
    @JoinColumn(name = "departure_airport_id")
    private Airport departureAirport;

    @ManyToOne(optional = false)
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrivalAirport;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FlightStatus status = FlightStatus.SCHEDULED;

    protected Flight() {
    }

    private Flight(String flightNumber, Airline airline, Airport departure, Airport arrival, LocalDateTime departureTime, LocalDateTime arrivalTime) {

        validateCreationRules(flightNumber, airline, departure, arrival, departureTime, arrivalTime);
        
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departureAirport = departure;
        this.arrivalAirport = arrival;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = FlightStatus.SCHEDULED;
    }

    //domain
    public static Flight createFlight(String flightNumber, Airline airline, Airport departure, Airport arrival, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        return new Flight(flightNumber, airline, departure, arrival, departureTime, arrivalTime);
    }

    private void validateCreationRules(String flightNumber, Airline airline, Airport departure, Airport arrival, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        if (flightNumber == null || flightNumber.isBlank()) {
            throw new BusinessException("Flight number is required");
        }
        if (!airline.isActive()) {
            throw new BusinessException("Cannot schedule flight for an suspended airline.");
        }
        if (departureTime.isAfter(arrivalTime)) {
            throw new BusinessException("Departure time must be before arrival time.");
        }
        if (departureTime.isEqual(arrivalTime)) {
            throw new BusinessException("Departure and arrival times cannot be the same.");
        }
        if (departure.equals(arrival)) {
            throw new BusinessException("Departure and arrival airports must be different.");
        }
        if (!departure.isOperational() || !arrival.isOperational()) {
            throw new BusinessException("Cannot use closed airport.");
        }
    }

    public void depart() {
        if (!isScheduled()) {
            throw new BusinessException("Only scheduled flights can depart.");
        }
        this.status = FlightStatus.IN_FLIGHT;
    }

    public void arrive() {
        if (!isInFlight()) {
            throw new BusinessException("Only in-flight flights can arrive.");
        }
        this.status = FlightStatus.ARRIVED;
    }

    public void cancel() {
        if (!isScheduled()) {
            throw new BusinessException("Only scheduled flights can be cancelled.");
        }
        this.status = FlightStatus.CANCELLED;
    }

    public boolean isScheduled() {
        return this.status == FlightStatus.SCHEDULED;
    }

    public boolean isInFlight() {
        return this.status == FlightStatus.IN_FLIGHT;
    }

    public boolean isActive() {
        return this.status == FlightStatus.SCHEDULED
                || this.status == FlightStatus.IN_FLIGHT;
    }

}
