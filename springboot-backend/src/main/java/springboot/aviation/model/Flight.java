package springboot.aviation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.FlightMessages;


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

    private static final String FLIGHT_NUMBER_PATTERN = "^[A-Z0-9]{2}\\d{1,4}$";

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
            throw new BusinessException(FlightMessages.FLIGHT_NUMBER_REQUIRED);
        }
        if(!flightNumber.matches(FLIGHT_NUMBER_PATTERN)) {
            throw new BusinessException(FlightMessages.FLIGHT_NUMBER_INVALID_FORMAT);
        }
        if(!airline.matchesFlightNumber(flightNumber)) {
            throw new BusinessException(FlightMessages.FLIGHT_NUMBER_MUST_MATCH_AIRLINE);
        }
        if (!airline.isActive()) {
            throw new BusinessException(FlightMessages.AIRLINE_ACTIVE);
        }
        if (departureTime.isAfter(arrivalTime)) {
            throw new BusinessException(FlightMessages.TIME_DEPARTURE_BEFORE);
        }
        if (departureTime.isEqual(arrivalTime)) {
            throw new BusinessException(FlightMessages.TIME_NOT_EQUALS);
        }
        if (departure.equals(arrival)) {
            throw new BusinessException(FlightMessages.AIRPORT_NOT_EQUALS);
        }
        if (!departure.isOperational() || !arrival.isOperational()) {
            throw new BusinessException(FlightMessages.AIRPORT_ACTIVE);
        }
    }

    public boolean hasFlightNumber(String flightNumber) {
        return this.flightNumber.equals(flightNumber);
    }

    public boolean hasAirline(Airline airline) {
        return this.airline.equals(airline);
    }

    public boolean hasDepartureAirport(Airport airport) {
        return this.departureAirport.equals(airport);
    }

    public boolean hasArrivalAirport(Airport airport) {
        return this.arrivalAirport.equals(airport);
    }

    public boolean hasDepartureTime(LocalDateTime departureTime) {
        return this.departureTime.equals(departureTime);
    }

    public boolean hasArrivalTime(LocalDateTime arrivalTime) {
        return this.arrivalTime.equals(arrivalTime);
    }

    public void depart() {
        if (!isScheduled()) {
            throw new BusinessException(FlightMessages.DEPART_ONLY_SCHEDULED);
        }
        this.status = FlightStatus.IN_FLIGHT;
    }

    public void arrive() {
        if (!isInFlight()) {
            throw new BusinessException(FlightMessages.ARRIVE_ONLY_IN_FLIGHT);
        }
        this.status = FlightStatus.ARRIVED;
    }

    public void cancel() {
        if (!isScheduled()) {
            throw new BusinessException(FlightMessages.CANCEL_ONLY_SCHEDULED);
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
