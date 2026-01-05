package springboot.aviation.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.FlightMessages;

@ExtendWith(MockitoExtension.class)
public class FlightTest {

    //support methods
    private Airline activateAirline() {
        return Airline.createAirline("G3", "Gol Airlines");
    }

    private Airport openAirport(String iata, String name, String city) {
        return Airport.createAirport(iata, name, city);
    }

    private Flight validFlight() {
        Airline airline = activateAirline();
        Airport departure = Airport.createAirport("DPT", "Dep Airport", "Departure City");
        Airport arrival = Airport.createAirport("ARR", "Arr Airport", "Arrival City");
        return Flight.createFlight(
                "G39206",
                airline,
                departure,
                arrival,
                LocalDateTime.of(2024, 7, 1, 10, 0),
                LocalDateTime.of(2024, 7, 1, 12, 0)
        );
    }

    //tests
    @Test
    void shouldCreateFlightSuccessfully() {
        Flight flight = validFlight();
        
        assertTrue(flight.hasFlightNumber("G39206"));
        assertTrue(flight.hasAirline(activateAirline()));
        assertTrue(flight.hasDepartureAirport(openAirport("DPT", "Dep Airport", "Departure City")));
        assertTrue(flight.hasArrivalAirport(openAirport("ARR", "Arr Airport", "Arrival City")));
        assertTrue(flight.hasDepartureTime(LocalDateTime.of(2024, 7, 1, 10, 0)));
        assertTrue(flight.hasArrivalTime(LocalDateTime.of(2024, 7, 1, 12, 0)));
        assertTrue(flight.isScheduled());
    }

    @Test
    void shouldNotCreateFlightIfFlightNumberIsNull() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                null,
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));

        assertEquals(FlightMessages.FLIGHT_NUMBER_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfFlightNumberIsMissing() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));

        assertEquals(FlightMessages.FLIGHT_NUMBER_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightForSuspendedAirline() {
        Airline airline = activateAirline();
        airline.suspend();

        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));

        assertEquals(FlightMessages.AIRLINE_ACTIVE, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfDepartureTimeIsAfterArrivalTime() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

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
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

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
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                dep,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));

        assertEquals(FlightMessages.AIRPORT_NOT_EQUALS, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfDepartureAirportIsClosed() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        dep.closeAirport();
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));

        assertEquals(FlightMessages.AIRPORT_ACTIVE, exception.getMessage());
    }

    @Test
    void shouldNotCreateFlightIfArrivalAirportIsClosed() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");
        arr.closeAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Flight.createFlight(
                "G39206",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
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
}
