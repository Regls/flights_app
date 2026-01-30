package springboot.aviation.domain.flight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import springboot.aviation.domain.airline.Airline;
import springboot.aviation.domain.airport.Airport;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.FlightMessages;


public class Flight {
    
    private final Long id;
    private final String flightNumber;
    private Airline airline;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private FlightStatus status;
    // private List<Booking> bookings = new ArrayList<>();

    private static final String FLIGHT_NUMBER_PATTERN = "^[A-Z0-9]{2}\\d{1,4}$";

    public Flight(Long id, String flightNumber, Airline airline,
            Airport departure, Airport arrival, LocalDateTime departureTime,
            LocalDateTime arrivalTime, FlightStatus status) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departureAirport = departure;
        this.arrivalAirport = arrival;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
    }

    public static Flight create(String flightNumber, Airline airline, Airport departure,
                            Airport arrival, LocalDateTime departureTime,
                            LocalDateTime arrivalTime) {
        validateCreationRules(flightNumber, airline, departure, arrival, departureTime, arrivalTime);
        return new Flight(null, flightNumber.toUpperCase(), airline, departure, arrival,
                            departureTime, arrivalTime, FlightStatus.SCHEDULED);
    }

    public static Flight restore(Long id, String flightNumber, Airline airline, Airport departure,
                            Airport arrival, LocalDateTime departureTime,
                            LocalDateTime arrivalTime, FlightStatus status) {
        return new Flight(id, flightNumber, airline, departure, arrival,
                            departureTime, arrivalTime, status);
    }

    private static void validateCreationRules(String flightNumber, Airline airline, Airport departure, Airport arrival, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        if (flightNumber == null || flightNumber.isBlank()) {
            throw new BusinessException(FlightMessages.FLIGHT_NUMBER_REQUIRED);
        }
        if (airline == null) {
            throw new BusinessException(FlightMessages.AIRLINE_REQUIRED);
        }
        if (departure == null) {
            throw new BusinessException(FlightMessages.DEPARTURE_AIRPORT_REQUIRED);
        }
        if (arrival == null) {
            throw new BusinessException(FlightMessages.ARRIVAL_AIRPORT_REQUIRED);
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
        if (!departure.isOpen() || !arrival.isOpen()) {
            throw new BusinessException(FlightMessages.AIRPORT_CLOSED);
        }
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

    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public Airline getAirline() { return airline; }
    public Airport getDepartureAirport() { return departureAirport; }
    public Airport getArrivalAirport() { return arrivalAirport; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public FlightStatus getStatus() { return status; }
    public boolean isScheduled() {return this.status == FlightStatus.SCHEDULED;}
    public boolean isInFlight() {return this.status == FlightStatus.IN_FLIGHT;}
}
