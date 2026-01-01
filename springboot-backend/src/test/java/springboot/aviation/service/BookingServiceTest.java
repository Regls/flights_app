package springboot.aviation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.dto.CreateBookingRequest;
import springboot.aviation.model.Airline;
import springboot.aviation.model.Airport;
import springboot.aviation.model.Booking;
import springboot.aviation.model.BookingStatus;
import springboot.aviation.model.Client;
import springboot.aviation.model.Flight;
import springboot.aviation.repository.BookingRepository;
import springboot.aviation.repository.ClientRepository;
import springboot.aviation.repository.FlightRepository;


@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private BookingService bookingService;

    //support methods
    private Client activateClient() {
        return Client.createClient("12345678901", "New", "Client");
    }

    private Flight activateFlight() {
        Airline airline = Airline.createAirline("TA", "Test Airline");
        Airport dep = Airport.createAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = Airport.createAirport("ARR", "Arr Airport", "Arrival City");
        return Flight.createFlight("FL123", airline, dep, arr,
                LocalDateTime.of(2024, 7, 1, 10, 0), LocalDateTime.of(2024, 7, 1, 12, 0)
        );
    }

    private void mockValidDependencies(Client client, Flight flight) {
        when(bookingRepository.existsByClientAndFlightAndStatusIn(any(Client.class), any(Flight.class), anyList())).thenReturn(false);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
    }

    private CreateBookingRequest validRequest() {
        CreateBookingRequest request = new CreateBookingRequest();
        request.clientId = 1L;
        request.flightId = 1L;
        return request;
    }

    //tests
    @Test
    void shouldReturnBookingList() {

        Booking booking = mock(Booking.class);

        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.findAll();

        assertEquals(1, bookings.size());
    }

    @Test
    void shouldReturnBookingById() {

        Booking booking = mock(Booking.class);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void shouldCreateBookingSucessfully() {

        Client client = activateClient();
        Flight flight = activateFlight();

        mockValidDependencies(client, flight);

        when(bookingRepository.save(any(Booking.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Booking booking = bookingService.createBooking(validRequest());

        assertNotNull(booking);
        assertTrue(booking.isCreated());
    }

    @Test
    void shouldNotCreateBookingWhenClientAlreadyHasBooking() {
        Client client = activateClient();
        Flight flight = activateFlight();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        when(bookingRepository.existsByClientAndFlightAndStatusIn(client, flight, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED))).thenReturn(true);

        assertThrows(BusinessException.class,
            () -> bookingService.createBooking(validRequest()));
    }

    @Test
    void shouldConfirmBookingSucessfully() {

        Booking booking = Booking.createBooking(activateClient(), activateFlight());

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.confirm(1L);

        assertTrue(booking.isConfirmed());
    }

    @Test
    void shouldNotConfirmWhenNonExistingBooking() {

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> bookingService.confirm(1L));
    }

    @Test
    void shouldCancelBookingSucessfully() {

        Booking booking = Booking.createBooking(activateClient(), activateFlight());

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.cancel(1L);

        assertTrue(booking.isCancelled());
    }

    @Test
    void shouldNotCancelWhenNonExistingBooking() {

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> bookingService.cancel(1L));
    }

}
