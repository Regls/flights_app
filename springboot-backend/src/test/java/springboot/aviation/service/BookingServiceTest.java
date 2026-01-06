package springboot.aviation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        return Client.createClient("12345678900", "New", "Client");
    }

    private Flight activateFlight() {
        Airline airline = Airline.createAirline("G3", "Gol Airlines");
        Airport dep = Airport.createAirport("DPT", "Dep Airport", "Departure City");
        Airport arr = Airport.createAirport("ARR", "Arr Airport", "Arrival City");
        return Flight.createFlight("G39206", airline, dep, arr,
                LocalDateTime.of(2024, 7, 1, 10, 0),
                LocalDateTime.of(2024, 7, 1, 12, 0)
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
        assertTrue(bookings.contains(booking));
        verify(bookingRepository).findAll();
    }

    @Test
    void shouldReturnBookingById() {

        Booking booking = mock(Booking.class);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking bookingFound = bookingService.findById(1L);

        assertEquals(booking, bookingFound);
        verify(bookingRepository).findById(1L);
    }

    @Test
    void shouldNotReturnBookingByIdWhenNonExistingBooking() {

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> bookingService.findById(1L));

        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository).findById(1L);
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
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void shouldNotCreateBookingWithNonExistingClient() {

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> bookingService.createBooking(validRequest()));

        assertEquals("Client not found", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldNotCreateBookingWithNonExistingFlight() {

        Client client = mock(Client.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> bookingService.createBooking(validRequest()));

        assertEquals("Flight not found", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldNotCreateBookingWhenClientAlreadyHasBooking() {
        Client client = activateClient();
        Flight flight = activateFlight();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        when(bookingRepository.existsByClientAndFlightAndStatusIn(client, flight, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED))).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> bookingService.createBooking(validRequest()));

        assertEquals("Client already has an active booking for this flight", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldConfirmBookingSucessfully() {

        Booking booking = mock(Booking.class);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(booking.isCreated()).thenReturn(true);

        bookingService.confirm(1L);

        verify(booking).confirm();
    }

    @Test
    void shouldNotConfirmWhenNonExistingBooking() {

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> bookingService.confirm(1L));

        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldNotConfirmWhenBookingAlreadyConfirmed() {

        Booking booking = mock(Booking.class);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(booking.isCreated()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> bookingService.confirm(1L));

        assertEquals("Only created bookings can be confirmed", exception.getMessage());
        verify(booking, never()).confirm();
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldCancelBookingSucessfully() {

        Booking booking = mock(Booking.class);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.cancel(1L);

        verify(booking).cancel();
        verify(bookingRepository).save(booking);
    }

    @Test
    void shouldNotCancelWhenNonExistingBooking() {

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> bookingService.cancel(1L));
        
        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldNotCancelWhenBookingAlreadyCancelled() {

        Booking booking = mock(Booking.class);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(booking.isCancelled()).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> bookingService.cancel(1L));

        assertEquals("Cancelled bookings cant be cancelled", exception.getMessage());
        verify(booking, never()).cancel();
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
