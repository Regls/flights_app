package springboot.aviation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.dto.ChangeAirportRequest;
import springboot.aviation.dto.CreateAirportRequest;
import springboot.aviation.model.Airport;
import springboot.aviation.model.Flight;
import springboot.aviation.model.FlightStatus;
import springboot.aviation.repository.AirportRepository;
import springboot.aviation.repository.FlightRepository;


@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private AirportService airportService;

    //support methods
    private CreateAirportRequest validRequest() {
        CreateAirportRequest request = new CreateAirportRequest();
        request.iataCode = "GRU";
        request.airportName = "Guarulhos International Airport";
        request.city = "São Paulo";
        return request;
    }

    private void mockValidDependencies() {
        when(airportRepository.existsByIataCode("GRU")).thenReturn(false);
    }

    private ChangeAirportRequest validChangeRequest() {
        ChangeAirportRequest request = new ChangeAirportRequest();
        request.airportName = "Guarulhos National Airport";
        return request;
    }

    //tests
    @Test
    void shouldReturnAirportList() {
        
        Airport airport = mock(Airport.class);
        when(airportRepository.findAll()).thenReturn(java.util.List.of(airport));

        List<Airport> airports = airportService.findAll();

        assertEquals(1, airports.size());
        assertTrue(airports.contains(airport));
        verify(airportRepository).findAll();
    }

    @Test
    void shouldReturnAirportById() {

        Airport airport = mock(Airport.class);

        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));
    
        Airport airportFound = airportService.findById(1L);

        assertEquals(airport, airportFound);
        verify(airportRepository).findById(1L);
    }

    @Test
    void shouldNotReturnAirportByIdWhenNonExistingAirport() {

        when(airportRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> airportService.findById(1L));
        
        assertEquals("Airport not found", exception.getMessage());
        verify(airportRepository).findById(1L);
    }

    @Test
    void shouldCreateAirportSuccessfully() {

        mockValidDependencies();

        when(airportRepository.save(any(Airport.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Airport airport = airportService.createAirport(validRequest());

        assertNotNull(airport);
        assertTrue(airport.isOperational());
        verify(airportRepository, times(1)).save(airport);
    }

    @Test
    void shouldNotCreateAirportWithDuplicatedIataCode() {

        when(airportRepository.existsByIataCode("GRU")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airportService.createAirport(validRequest()));

        assertEquals("Airport with IATA code already exists", exception.getMessage());
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void shouldChangeAirportNameSucessfully() {

        Airport airport = mock(Airport.class);

        ChangeAirportRequest request = validChangeRequest();

        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));

        airportService.changeAirportName(1L, request);

        verify(airport).changeName("Guarulhos National Airport");
        verify(airportRepository).save(airport);
    }

    @Test
    void shouldNotChangeAirportNameWhenNonExistingAirport() {

        when(airportRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> airportService.changeAirportName(1L, validChangeRequest()));

        assertEquals("Airport not found", exception.getMessage());
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void shouldOpenAirportSuccessfully() {

        Airport airport = mock(Airport.class);

        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));

        airportService.open(1L);

        verify(airport).open();
        verify(airportRepository).save(airport);
    }

    @Test
    void shouldNotOpenAirportWhenNonExistingAirport() {

        when(airportRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> airportService.open(1L));

        assertEquals("Airport not found", exception.getMessage());
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void shouldNotOpenAirportWhenAlreadyOpen() {

        Airport airport = mock(Airport.class);

        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));
        when(airport.isOperational()).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airportService.open(1L));

        assertEquals("Airport is already open", exception.getMessage());
        verify(airport, never()).open();
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void shouldCloseAirportSuccessfully() {

        Airport airport = mock(Airport.class);

        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));
        when(airport.isOperational()).thenReturn(true);

        airportService.close(1L);

        verify(airport).close();
        verify(airportRepository).save(airport);
    }

    @Test
    void shouldNotCloseAirportWhenNonExistingAirport() {

        when(airportRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> airportService.close(1L));

        assertEquals("Airport not found", exception.getMessage());
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void shouldNotCloseAirportWhenAlreadyClosed() {

        Airport airport = mock(Airport.class);

        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));
        when(airport.isOperational()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airportService.close(1L));

        assertEquals("Airport is already closed", exception.getMessage());
        verify(airport, never()).close();
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void shouldCancelAllFlightsWhenAirportIsClosed() {
        Airport airport = mock(Airport.class);
        Flight flight1 = mock(Flight.class);
        Flight flight2 = mock(Flight.class);

        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));
        when(airport.isOperational()).thenReturn(true);

        when(flightRepository.findByDepartureAirportOrArrivalAirportAndStatus(airport, airport, FlightStatus.SCHEDULED))
            .thenReturn(List.of(flight1, flight2));

        airportService.close(1L);

        verify(airport).close();
        verify(flight1).cancel();
        verify(flight2).cancel();
        verify(flightRepository).saveAll(anyList());
        verify(airportRepository).save(airport);
    }

    @Test
    void shouldNotCancelFlightsWhenNoScheduledWhenAirportIsClosed() {
        Airport airport = mock(Airport.class);

        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));
        when(airport.isOperational()).thenReturn(true);

        when(flightRepository.findByDepartureAirportOrArrivalAirportAndStatus(airport, airport, FlightStatus.SCHEDULED)).thenReturn(List.of());

        airportService.close(1L);

        verify(flightRepository).saveAll(List.of());
        verify(airport).close();
        verify(airportRepository).save(airport);
    }
    
}
