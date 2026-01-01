package springboot.aviation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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
import springboot.aviation.dto.CreateFlightRequest;
import springboot.aviation.model.Flight;
import springboot.aviation.model.Airline;
import springboot.aviation.model.Airport;
import springboot.aviation.model.Booking;
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
        return Airline.createAirline("Test Airline", "TA");
    }

    private Airport openAirport(String iata, String name, String city) {
        return Airport.createAirport(iata, name, city);
    }

    private void mockValidDependencies(Airline airline, Airport dep, Airport arr) {
        when(flightRepository.existsByFlightNumber("FL123")).thenReturn(false);
        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airportRepository.findById(1L)).thenReturn(Optional.of(dep));
        when(airportRepository.findById(2L)).thenReturn(Optional.of(arr));
    }

    private CreateFlightRequest validRequest() {
        CreateFlightRequest request = new CreateFlightRequest();
        request.flightNumber = "FL123";
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
    }

    @Test
    void shouldReturnFlightById() {

        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        Flight result = flightService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void shouldCreateFlightSuccessfully() {

        Airline airline = activateAirline();
        Airport dep = openAirport("GRU", "Guarulhos Airport", "Sao Paulo");
        Airport arr = openAirport("JFK", "John Kennedy Airport", "newyork");

        mockValidDependencies(airline, dep, arr);

        when(flightRepository.save(any(Flight.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Flight flight = flightService.createFlight(validRequest());

        assertNotNull(flight);
        assertTrue(flight.isScheduled());
    }

    @Test
    void shouldNotCreateFlightWithDuplicatedFlightNumber() {

        when(flightRepository.existsByFlightNumber("FL123")).thenReturn(true);

        assertThrows(BusinessException.class,
            () -> flightService.createFlight(validRequest()));
    }

    @Test
    void shouldNotDepartWhenNonExistingFlight() {

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> flightService.depart(1L));
    }

    @Test
    void shouldDepartScheduledFlight() {

        Flight flight = mock(Flight.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        flightService.depart(1L);

        verify(flight).depart();
        verify(flightRepository).save(flight);
    }


    @Test
    void shouldCancelAllBookingsWhenFlightIsCancelled() {
        Flight flight = mock(Flight.class);
        Booking booking1 = mock(Booking.class);
        Booking booking2 = mock(Booking.class);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        when(bookingRepository.findByFlight(flight)).thenReturn(List.of(booking1, booking2));

        flightService.cancel(1L);

        verify(flight).cancel();
        verify(booking1).cancel();
        verify(booking2).cancel();
        verify(bookingRepository).saveAll(anyList());
    }
}
