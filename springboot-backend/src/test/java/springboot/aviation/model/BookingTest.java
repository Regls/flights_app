/* package springboot.aviation.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.BookingMessages;

@ExtendWith(MockitoExtension.class)
public class BookingTest {
    
    //support methods
    private Client activateClient() {
        return Client.createClient("12345678900", "Test", "Client");
    }

    private Flight activateFlight() {
        Airline airline = Airline.createAirline("G3", "Gol Airlines");
        Airport dep = Airport.createAirport("DPT", "Dpt Airport", "Departure City");
        Airport arr = Airport.createAirport("ARR", "Arr Airport", "Arrival City");
        return Flight.createFlight("G39206", airline, dep, arr,
                LocalDateTime.of(2024, 7, 1, 10, 0),
                LocalDateTime.of(2024, 7, 1, 12, 0)
        );
    }

    private Booking validBooking() {
        return Booking.createBooking(activateClient(), activateFlight());
    }

    //tests
    @Test
    void shouldCreateBookingSucessfully() {
        Booking booking = validBooking();

        assertTrue(booking.hasClient("12345678900"));
        assertTrue(booking.hasFlight("G39206"));
        assertTrue(booking.isCreated());
    }

    @Test
    void shouldNotCreateBookingIfClientIsNull() {
        
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Booking.createBooking(null, activateFlight()));

        assertEquals(BookingMessages.CLIENT_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateBookingIfClientIsNotActive() {
        Client client = activateClient();
        client.deactivate();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> Booking.createBooking(client, activateFlight()));

        assertEquals(BookingMessages.CLIENT_ACTIVE, exception.getMessage());
    }

    @Test
    void shouldNotCreateBookingIfFlightIsNull() {
        
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Booking.createBooking(activateClient(), null));

        assertEquals(BookingMessages.FLIGHT_REQUIRED, exception.getMessage());
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
        
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Booking.createBooking(activateClient(), flight));

        assertEquals(BookingMessages.FLIGHT_SCHEDULED, exception.getMessage());
    }

    @Test
    void shouldChangeStatusToConfirmed() {
        Booking booking = validBooking();
        booking.confirm();

        assertTrue(booking.isConfirmed());
    }

    @Test
    void shouldNotConfirmIfBookingIsAlreadyConfirmed() {
        Booking booking = validBooking();
        booking.confirm();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> booking.confirm());

        assertEquals(BookingMessages.CONFIRM_ONLY_CREATED, exception.getMessage());
    }

    @Test
    void shouldNotConfirmIfBookingIsAlreadyCancelled() {
        Booking booking = validBooking();
        booking.cancel();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> booking.confirm());

        assertEquals(BookingMessages.CANCELLED_CANNOT_CHANGE, exception.getMessage());
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

        BusinessException exception = assertThrows(BusinessException.class,
            () -> booking.confirm());

        assertEquals(BookingMessages.CONFIRM_ONLY_FLIGHT_SCHEDULED, exception.getMessage());
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

        BusinessException exception = assertThrows(BusinessException.class,
            () -> booking.confirm());

        assertEquals(BookingMessages.CANCELLED_CANNOT_CHANGE, exception.getMessage());
    }

    @Test
    void shouldNotCancelIfBookingIsCancelled() {
        Booking booking = validBooking();
        booking.cancel();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> booking.cancel());
        
        assertEquals(BookingMessages.CANCEL_ONLY_CREATED_OR_CONFIRMED, exception.getMessage());
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

        BusinessException exception = assertThrows(BusinessException.class,
            () -> booking.cancel());

        assertEquals(BookingMessages.CANCEL_ONLY_FLIGHT_SCHEDULED, exception.getMessage());
    }
}
 */