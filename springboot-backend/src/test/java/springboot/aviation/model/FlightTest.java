package springboot.aviation.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class FlightTest {

    //support methods
    private Airline activateAirline() {
        return Airline.createAirline("TA", "Test Airline");
    }

    private Airport openAirport(String iata, String name, String city) {
        return Airport.createAirport(iata, name, city);
    }

    private Flight validFlight() {
        Airline airline = Airline.createAirline("TA", "Test Airline");
        Airport departure = Airport.createAirport("DPT", "Dep Airport", "Departure City");
        Airport arrival = Airport.createAirport("ARR", "Arr Airport", "Arrival City");
        return Flight.createFlight(
                "FL123",
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
        
        assertTrue(flight.isScheduled());
    }

    @Test
    void shouldNotCreateFlightIfFlightNumberIsNull() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        assertThrows(BusinessException.class, () -> Flight.createFlight(
                null,
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));
    }

    @Test
    void shouldNotCreateFlightIfFlightNumberIsMissing() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        assertThrows(BusinessException.class, () -> Flight.createFlight(
                "",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));
    }

    @Test
    void shouldNotCreateFlightForSuspendedAirline() {
        Airline airline = activateAirline();
        airline.suspend();

        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        assertThrows(BusinessException.class, () -> Flight.createFlight(
                "FL123",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));
    }

    @Test
    void shouldNotCreateFlightIfDepartureTimeIsAfterArrivalTime() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        assertThrows(BusinessException.class, () -> Flight.createFlight(
                "FL123",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 14, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));
    }

    @Test
    void shouldNotCreateFlightIfDepartureAndArrivalTimeAreTheSame() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        assertThrows(BusinessException.class, () -> Flight.createFlight(
                "FL123",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 12, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));
    }

    @Test
    void shouldNotCreateFlightIfDepartureAndArrivalAirportsAreTheSame() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");

        assertThrows(BusinessException.class, () -> Flight.createFlight(
                "FL123",
                airline,
                dep,
                dep,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));
    }

    @Test
    void shouldNotCreateFlightIfDepartureAirportIsClosed() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        dep.closeAirport();
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");

        assertThrows(BusinessException.class, () -> Flight.createFlight(
                "FL123",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));
    }

    @Test
    void shouldNotCreateFlightIfArrivalAirportIsClosed() {
        Airline airline = activateAirline();
        Airport dep = openAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = openAirport("ARR", "Arr Airport", "Arrival City");
        arr.closeAirport();

        assertThrows(BusinessException.class, () -> Flight.createFlight(
                "FL123",
                airline,
                dep,
                arr,
                LocalDateTime.of(2024, 7, 2, 10, 0),
                LocalDateTime.of(2024, 7, 2, 12, 0)
        ));
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

        assertThrows(BusinessException.class, () -> flight.depart());
    }

    @Test
    void shouldNotArriveIfNotInFlight() {
        Flight flight = validFlight();

        assertThrows(BusinessException.class, () -> flight.arrive());
    }

    @Test
    void shouldNotCancelIfNotScheduled() {
        Flight flight = validFlight();
        flight.depart();

        assertThrows(BusinessException.class, () -> flight.cancel());
    }

    @Test
    void shouldNotCancelAfterArrived(){
        Flight flight = validFlight();
        flight.depart();
        flight.arrive();

        assertThrows(BusinessException.class, () -> flight.cancel());
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
