/* package springboot.aviation.domain.flight;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.airline.Airline;
import springboot.aviation.domain.airport.Airport;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.FlightMessages;


public class FlightTest {

    //support methods
    private Client validClient() {
        return Client.create("12345678901", "Valid", "Client");
    }

    private Airline activateAirline() {
        return Airline.create("G3", "Gol Airlines");
    }

    private Airport openDepAirport() {
        return Airport.create("DPT", "Dep Airport", "Departure City");
    }

    private Airport openArrAirport() {
        return Airport.create("ARR", "Arr Airport", "Arrival City");
    }

    private LocalDateTime depDateTime(){
        return LocalDateTime.of(2024, 7, 1, 10, 0);
    }

    private LocalDateTime arrDateTime(){
        return LocalDateTime.of(2024, 7, 1, 12, 0);
    }

    private Flight validFlight() {
        Airline airline = activateAirline();
        Airport departure = openDepAirport();
        Airport arrival = openArrAirport();
        return Flight.create(
                "G39206",
                airline,
                departure,
                arrival,
                depDateTime(),
                arrDateTime()
        );
    }

    private Booking validBooking(Flight Flight) {
        Client client = validClient();
        Booking booking = Booking.createBooking(client, Flight);
        return booking;
    }

    //tests
    @Test
    void shouldCreateFlightSuccessfully() {
        Flight flight = validFlight();
        
        assertTrue(flight.hasFlightNumber("G39206"));
        assertTrue(flight.hasAirline(Airline.createAirline("G3", "Gol Airlines")));
        assertTrue(flight.hasDepartureAirport(Airport.createAirport("DPT", "Dep Airport", "Departure city")));
        assertTrue(flight.hasArrivalAirport(Airport.createAirport("ARR", "Arr Airport", "Arrival City")));
        assertTrue(flight.hasDepartureTime(LocalDateTime.of(2024, 7, 1, 10, 0)));
        assertTrue(flight.hasArrivalTime(LocalDateTime.of(2024, 7, 1, 12, 0)));
        assertTrue(flight.isScheduled());
    }

    @Test
    void shouldNotCreateFlightIfFlightNumberIsNull() {
        Airline airline = activateAirline();
        Airport dep = openDepAirport();
        Airport arr = openArrAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                null,
                airline,
                dep,
                arr,
                depDateTime(),
                arrDateTime()
        ));

        assertEquals(FlightMessages.FLIGHT_NUMBER_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfFlightNumberIsMissing() {
        Airline airline = activateAirline();
        Airport dep = openDepAirport();
        Airport arr = openArrAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "",
                airline,
                dep,
                arr,
                depDateTime(),
                arrDateTime()
        ));

        assertEquals(FlightMessages.FLIGHT_NUMBER_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfAirlineIsNull(){
        Airline airline = null;
        Airport dep = openDepAirport();
        Airport arr = openArrAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                depDateTime(),
                arrDateTime()
        ));

        assertEquals(FlightMessages.AIRLINE_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfDepartureAirportIsNull(){
        Airline airline = activateAirline();
        Airport dep = null;
        Airport arr = openArrAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                depDateTime(),
                arrDateTime()
        ));

        assertEquals(FlightMessages.DEPARTURE_AIRPORT_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfArrivalAirportIsNull(){
        Airline airline = activateAirline();
        Airport dep = openDepAirport();
        Airport arr = null;

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                depDateTime(),
                arrDateTime()
        ));

        assertEquals(FlightMessages.ARRIVAL_AIRPORT_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightForSuspendedAirline() {
        Airline airline = activateAirline();
        airline.suspend();

        Airport dep = openDepAirport();
        Airport arr = openArrAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                depDateTime(),
                arrDateTime()
        ));

        assertEquals(FlightMessages.AIRLINE_ACTIVE, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfDepartureTimeIsAfterArrivalTime() {
        Airline airline = activateAirline();
        Airport dep = openDepAirport();
        Airport arr = openArrAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 14, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));

        assertEquals(FlightMessages.TIME_DEPARTURE_BEFORE, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfDepartureAndArrivalTimeAreTheSame() {
        Airline airline = activateAirline();
        Airport dep = openDepAirport();
        Airport arr = openArrAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 12, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));

        assertEquals(FlightMessages.TIME_NOT_EQUALS, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfDepartureAndArrivalAirportsAreTheSame() {
        Airline airline = activateAirline();
        Airport dep = openDepAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                dep,
                depDateTime(),
                arrDateTime()
        ));

        assertEquals(FlightMessages.AIRPORT_NOT_EQUALS, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfDepartureAirportIsClosed() {
        Airline airline = activateAirline();
        Airport dep = openDepAirport();
        dep.close();
        Airport arr = openArrAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                depDateTime(),
                arrDateTime()
        ));

        assertEquals(FlightMessages.AIRPORT_ACTIVE, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfArrivalAirportIsClosed() {
        Airline airline = activateAirline();
        Airport dep = openDepAirport();
        Airport arr = openArrAirport();
        arr.close();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                depDateTime(),
                arrDateTime()
        ));

        assertEquals(FlightMessages.AIRPORT_ACTIVE, exception.getMessage());
    }

    @Test
    void shouldChangeStatusToInFlightWhenDeparting() {
        Flight flight = validFlight();
        flight.depart();
        
        assertTrue(flight.isInFlight());
    }

    @Test
    void shouldNotDepartIfAlreadyInFlight() {
        Flight flight = validFlight();
        flight.depart();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> flight.depart());

        assertEquals(FlightMessages.DEPART_ONLY_SCHEDULED, exception.getMessage());
    }

    @Test
    void shouldNotArriveIfNotInFlight() {
        Flight flight = validFlight();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> flight.arrive());

        assertEquals(FlightMessages.ARRIVE_ONLY_IN_FLIGHT, exception.getMessage());
    }

    @Test
    void shouldNotCancelIfNotScheduled() {
        Flight flight = validFlight();
        flight.depart();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> flight.cancel());

        assertEquals(FlightMessages.CANCEL_ONLY_SCHEDULED, exception.getMessage());
    }

    @Test
    void shouldNotCancelAfterArrived(){
        Flight flight = validFlight();
        flight.depart();
        flight.arrive();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> flight.cancel());

        assertEquals(FlightMessages.CANCEL_ONLY_SCHEDULED, exception.getMessage());
    }

    @Test
    void shouldBeActiveWhenScheduledOrInFlight() {
        Flight flight = validFlight();
        assertTrue(flight.isActive());

        flight.depart();
        assertTrue(flight.isActive());

        flight.arrive();
        assertFalse(flight.isActive());
    }

    @Test
    void shouldCancelBookingWhenFlightIsCancelled() {
        Flight flight = validFlight();
        Booking booking = validBooking(flight);

        flight.cancel();

        assertTrue(booking.isCancelled());
    }

} */