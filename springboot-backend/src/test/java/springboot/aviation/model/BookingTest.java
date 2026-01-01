package springboot.aviation.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class BookingTest {
    
    //support methods
    private Client activateClient() {
        return Client.createClient("12345678900", "Test", "Client");
    }

    private Flight activateFlight() {
        Airline airline = Airline.createAirline("TA", "Test Airline");
        Airport dep = Airport.createAirport("DPT", "Dpt Airport", "Departure City");
        Airport arr = Airport.createAirport("ARR", "Arr Airport", "Arrival City");
        return Flight.createFlight("FL123", airline, dep, arr,
                LocalDateTime.of(2024, 7, 1, 10, 0), LocalDateTime.of(2024, 7, 1, 12, 0)
        );
    }

    private Booking validBooking() {
        return Booking.createBooking(activateClient(), activateFlight());
    }

    //tests
    @Test
    void shouldCreateBookingSucessfully() {
        Booking booking = validBooking();

        assertTrue(booking.isCreated());
    }

    @Test
    void shouldNotCreateBookingIfClientIsNull() {
        
        assertThrows(BusinessException.class, () -> Booking.createBooking(null, activateFlight()));
    }

    @Test
    void shouldNotCreateBookingIfClientIsNotActive() {
        Client client = activateClient();
        client.deactivate();

        assertThrows(BusinessException.class, () -> Booking.createBooking(client, activateFlight()));
    }

    @Test
    void shouldNotCreateBookingIfFlightIsNull() {
        
        assertThrows(BusinessException.class, () -> Booking.createBooking(activateClient(), null));
    }

    @ParameterizedTest
    @EnumSource(value = FlightStatus.class, names = {"IN_FLIGHT", "ARRIVED", "CANCELLED"})
    void shouldNotCreateBookingIfFlightIsNotScheduled(FlightStatus status) {
        Flight flight = activateFlight();

        switch (status) {
            case IN_FLIGHT -> flight.depart();
            case ARRIVED -> {
                flight.depart();
                flight.arrive();
            }
            case CANCELLED -> flight.cancel();
            default -> throw new BusinessException("Unexpected value: " + status);
        }
        
        assertThrows(BusinessException.class, () -> Booking.createBooking(activateClient(), flight));
    }

    @Test
    void shouldChangeStatusToConfirmed() {
        Booking booking = validBooking();
        booking.confirm();

        assertTrue(booking.isConfirmed());
    }

    @ParameterizedTest
    @EnumSource(value = BookingStatus.class, names = {"CONFIRMED", "CANCELLED"})
    void shouldNotConfirmIfBookingIsNotCreated(BookingStatus status) {
        Booking booking = validBooking();

        switch (status) {
            case CONFIRMED -> booking.confirm();
            case CANCELLED -> booking.cancel();
            default -> throw new BusinessException("Unexpected value: " + status);
        }

        assertThrows(BusinessException.class, () -> booking.confirm());
    }

    @ParameterizedTest
    @EnumSource(value = FlightStatus.class, names = {"IN_FLIGHT", "ARRIVED", "CANCELLED"})
    void shouldNotConfirmIfFlightIsNotScheduled(FlightStatus status) {
        Client client = activateClient();
        Flight flight = activateFlight();
        Booking booking = Booking.createBooking(client, flight);

        switch (status) {
            case IN_FLIGHT -> flight.depart();
            case ARRIVED -> {
                flight.depart();
                flight.arrive();
            }
            case CANCELLED -> flight.cancel();
            default -> throw new BusinessException("Unexpected value: " + status);
        }

        assertThrows(BusinessException.class, () -> booking.confirm());
    }

    @Test
    void shouldChangeStatusToCancelled() {
        Booking booking = validBooking();
        booking.cancel();

        assertTrue(booking.isCancelled());
    }

    @Test
    void shouldNotChangeStatusAfterCancelled() {
        Booking booking = validBooking();
        booking.cancel();

        assertThrows(BusinessException.class, () -> booking.confirm());
    }

    @Test
    void shouldNotCancelIfBookingIsCancelled() {
        Booking booking = validBooking();
        booking.cancel();

        assertThrows(BusinessException.class, () -> booking.cancel());
    }

    @Test
    void shouldAllowCancelAfterConfirm() {
        Booking booking = validBooking();
        booking.confirm();
        booking.cancel();

        assertTrue(booking.isCancelled());
    }

    @ParameterizedTest
    @EnumSource(value = FlightStatus.class, names = {"IN_FLIGHT", "ARRIVED"})
    void shouldNotCancelIfFlightIsNotScheduled(FlightStatus status) {
        Client client = activateClient();
        Flight flight = activateFlight();
        Booking booking = Booking.createBooking(client, flight);

        booking.confirm();

        switch (status) {
            case IN_FLIGHT -> flight.depart();
            case ARRIVED -> {
                flight.depart();
                flight.arrive();
            }
            default -> throw new BusinessException("Unexpected value: " + status);
        }

        assertThrows(BusinessException.class, () -> booking.cancel());
    }
}
