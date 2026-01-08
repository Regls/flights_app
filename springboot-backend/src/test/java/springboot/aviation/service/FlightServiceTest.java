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

import springboot.aviation.dto.request.CreateFlightRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Flight;
import springboot.aviation.model.Airline;
import springboot.aviation.model.Airport;
import springboot.aviation.model.Booking;
import springboot.aviation.model.BookingStatus;
import springboot.aviation.repository.FlightRepository;
import springboot.aviation.repository.AirlineRepository;
import springboot.aviation.repository.AirportRepository;
import springboot.aviation.repository.BookingRepository;


@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {
    
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private FlightService flightService;

    //support methods
    private Airline activateAirline() {
        return Airline.createAirline("G3", "Gol Airlines");
    }

    private Airport openDepAirport() {
        return Airport.createAirport("DPT", "Dep Airport", "Departure City");
    }

    private Airport openArrAirport() {
        return Airport.createAirport("ARR", "Arr Airport", "Arrival City");
    }

    private void mockValidDependencies(Airline airline, Airport dep, Airport arr) {
        when(flightRepository.existsByFlightNumber("G39206")).thenReturn(false);
        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airportRepository.findById(1L)).thenReturn(Optional.of(dep));
        when(airportRepository.findById(2L)).thenReturn(Optional.of(arr));
    }

    private CreateFlightRequest validRequest() {
        CreateFlightRequest request = new CreateFlightRequest();
        request.flightNumber = "G39206";
        request.airlineId = 1L;
        request.departureAirportId = 1L;
        request.arrivalAirportId = 2L;
        request.departureTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        request.arrivalTime = LocalDateTime.of(2024, 1, 1, 12, 0);
        return request;
    }

    //tests
    @Test
    void shouldReturnFlightList() throws Exception {

        Flight flight = mock(Flight.class);
        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<Flight> flights = flightService.findAll();

        assertEquals(1, flights.size());
        assertTrue(flights.contains(flight));
        verify(flightRepository).findAll();
    }

    @Test
    void shouldReturnFlightById() {

        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        Flight flightFound = flightService.findById(1L);

        assertEquals(flight, flightFound);
        verify(flightRepository).findById(1L);
    }

    @Test
    void shouldNotReturnFlightByIdWhenNonExistingFlight() {

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> flightService.findById(1L));
        
        assertEquals("Flight not found", exception.getMessage());
        verify(flightRepository).findById(1L);
    }

    @Test
    void shouldCreateFlightSuccessfully() {

        Airline airline = activateAirline();
        Airport dep = openDepAirport();
        Airport arr = openArrAirport();

        mockValidDependencies(airline, dep, arr);

        when(flightRepository.save(any(Flight.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Flight flight = flightService.createFlight(validRequest());

        assertNotNull(flight);
        assertTrue(flight.isScheduled());
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void shouldNotCreateFlightWithNonExistingAirline() {

        when(airlineRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> flightService.createFlight(validRequest()));
        
        assertEquals("Airline not found", exception.getMessage());
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldNotCreateFlightWithNonExistingDepartureAirport() {

        Airline airline = activateAirline();

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airportRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> flightService.createFlight(validRequest()));

        assertEquals("Departure airport not found", exception.getMessage());
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldNotCreateFlightWithNonExistingArrivalAirport() {

        Airline airline = activateAirline();
        Airport dep = openDepAirport();

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airportRepository.findById(1L)).thenReturn(Optional.of(dep));
        when(airportRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> flightService.createFlight(validRequest()));

        assertEquals("Arrival airport not found", exception.getMessage());
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldNotCreateFlightWithDuplicatedFlightNumber() {

        Airline airline = activateAirline();
        Airport dep = openDepAirport();
        Airport arr = openArrAirport();

        mockValidDependencies(airline, dep, arr);

        when(flightRepository.existsByFlightNumber("G39206")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> flightService.createFlight(validRequest()));

        assertEquals("Flight with flight number already exists", exception.getMessage());
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldDepartScheduledFlight() {

        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flight.isScheduled()).thenReturn(true);

        flightService.depart(1L);

        verify(flight).depart();
        verify(flightRepository).save(flight);
    }

    @Test
    void shouldNotDepartWhenNonExistingFlight() {

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> flightService.depart(1L));

        assertEquals("Flight not found", exception.getMessage());
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldNotDepartWhenNonScheduledFlight() {

        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flight.isScheduled()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> flightService.depart(1L));

        assertEquals("Only scheduled flights can depart", exception.getMessage());
        verify(flight, never()).depart();
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldArriveInFlight() {

        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flight.isInFlight()).thenReturn(true);

        flightService.arrive(1L);

        verify(flight).arrive();
        verify(flightRepository).save(flight);
    }

    @Test
    void shouldNotArriveWhenNonExistingFlight() {

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> flightService.arrive(1L));

        assertEquals("Flight not found", exception.getMessage());
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldNotArriveWhenNonInFlightFlight() {

        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flight.isInFlight()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> flightService.arrive(1L));

        assertEquals("Only in-flight flights can arrive", exception.getMessage());
        verify(flight, never()).arrive();
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldCancelScheduledFlight() {

        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flight.isScheduled()).thenReturn(true);

        flightService.cancel(1L);

        verify(flight).cancel();
        verify(flightRepository).save(flight);
    }

    @Test
    void shouldNotCancelWhenNonExistingFlight() {

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> flightService.cancel(1L));

        assertEquals("Flight not found", exception.getMessage());
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldNotCancelWhenNonScheduledFlight() {

        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flight.isScheduled()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> flightService.cancel(1L));

        assertEquals("Only scheduled flights can be cancelled", exception.getMessage());
        verify(flight, never()).cancel();
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldCancelAllBookingsWhenFlightIsCancelled() {
        Flight flight = mock(Flight.class);
        Booking booking1 = mock(Booking.class);
        Booking booking2 = mock(Booking.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flight.isScheduled()).thenReturn(true);

        when(bookingRepository.findByFlightAndStatusIn(flight, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED)))
                .thenReturn(List.of(booking1, booking2));

        flightService.cancel(1L);

        verify(flight).cancel();
        verify(booking1).cancel();
        verify(booking2).cancel();
        verify(bookingRepository).saveAll(anyList());
        verify(flightRepository).save(flight);
    }

    @Test
    void shouldNotCancelBookingsWhenNoCreatedOrConfirmedWhenFlightIsCancelled() {
        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flight.isScheduled()).thenReturn(true);

        when(bookingRepository.findByFlightAndStatusIn(flight, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED))).thenReturn(List.of());

        flightService.cancel(1L);

        verify(bookingRepository).saveAll(List.of());
        verify(flight).cancel();
        verify(flightRepository).save(flight);
    }
}
