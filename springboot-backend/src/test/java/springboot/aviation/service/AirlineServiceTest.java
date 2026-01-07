package springboot.aviation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import springboot.aviation.dto.ChangeAirlineRequest;
import springboot.aviation.dto.CreateAirlineRequest;
import springboot.aviation.model.Flight;
import springboot.aviation.model.FlightStatus;
import springboot.aviation.model.Airline;
import springboot.aviation.repository.AirlineRepository;
import springboot.aviation.repository.FlightRepository;


@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {
    
    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private AirlineService airlineService;

    //support methods
    private CreateAirlineRequest validRequest() {
        CreateAirlineRequest request = new CreateAirlineRequest();
        request.iataCode = "G3";
        request.airlineName = "Gol Airlines";
        return request;
    }

    private void mockValidDependencies() {
        when(airlineRepository.existsByIataCode("G3")).thenReturn(false);
    }

    private ChangeAirlineRequest validChangeRequest() {
        ChangeAirlineRequest request = new ChangeAirlineRequest();
        request.airlineName = "Azul Airlines";
        return request;
    }

    //tests
    @Test
    void shouldReturnAirlineList() {

        Airline airline = mock(Airline.class);
        when(airlineRepository.findAll()).thenReturn(List.of(airline));

        List<Airline> airlines = airlineService.findAll();

        assertEquals(1, airlines.size());
        assertTrue(airlines.contains(airline));
        verify(airlineRepository).findAll();
    }

    @Test
    void shouldReturnAirlineById() {

        Airline airline = mock(Airline.class);

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));

        Airline airlineFound = airlineService.findById(1L);

        assertEquals(airline, airlineFound);
        verify(airlineRepository).findById(1L);
    }

    @Test
    void shouldNotReturnAirlineByIdWhenNonExistingAirline() {

        when(airlineRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> airlineService.findById(1L));
        
        assertEquals("Airline not found", exception.getMessage());
        verify(airlineRepository).findById(1L);
    }

    @Test
    void shouldCreateAirlineSuccessfully() {

        mockValidDependencies();

        when(airlineRepository.save(any(Airline.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Airline airline = airlineService.createAirline(validRequest());

        assertNotNull(airline);
        assertTrue(airline.isActive());
        verify(airlineRepository, times(1)).save(airline);
    }

    @Test
    void shouldNotCreateAirlineWithDuplicatedIataCode() {

        when(airlineRepository.existsByIataCode("G3")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airlineService.createAirline(validRequest()));

        assertEquals("Airline with IATA code already exists.", exception.getMessage());
        verify(airlineRepository, never()).save(any(Airline.class));
    }

    @Test
    void shouldChangeAirlineNameSucessfully() {

        Airline airline = mock(Airline.class);

        ChangeAirlineRequest request = validChangeRequest();

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));

        airlineService.changeAirlineName(1L, request);

        verify(airline).changeName("Azul Airlines");
        verify(airlineRepository).save(airline);
    }

    @Test
    void shouldNotChangeAirlineNameWhenNonExistingAirline() {
        when(airlineRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> airlineService.changeAirlineName(1L, validChangeRequest()));

        assertEquals("Airline not found", exception.getMessage());
        verify(airlineRepository, never()).save(any(Airline.class));
    }

    @Test
    void shouldActivateAirlineSucessfully() {

        Airline airline = mock(Airline.class);

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));

        airlineService.activate(1L);

        verify(airline).activate();
    }

    @Test
    void shouldNotActivateAirlineWhenNonExistingAirline() {

        when(airlineRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> airlineService.activate(1L));

        assertEquals("Airline not found", exception.getMessage());
        verify(airlineRepository, never()).save(any(Airline.class));
    }

    @Test
    void shouldNotActivateAirlineWhenAlreadyActive() {

        Airline airline = mock(Airline.class);

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airline.isActive()).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airlineService.activate(1L));

        assertEquals("Airline is already active", exception.getMessage());
        verify(airline, never()).activate();
        verify(airlineRepository, never()).save(any(Airline.class));
    }
    
    @Test
    void shouldSuspendAirlineSucessfully() {

        Airline airline = mock(Airline.class);

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airline.isActive()).thenReturn(true);

        airlineService.suspend(1L);

        verify(airline).suspend();
        verify(airlineRepository).save(airline);
    }

    @Test
    void shouldNotSuspendAirlineWhenNonExistingAirline() {

        when(airlineRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> airlineService.suspend(1L));

        assertEquals("Airline not found", exception.getMessage());
        verify(airlineRepository, never()).save(any(Airline.class));
    }

    @Test
    void shouldNotSuspendAirlineWhenAlreadySuspended() {

        Airline airline = mock(Airline.class);

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airline.isActive()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airlineService.suspend(1L));

        assertEquals("Airline is already suspended", exception.getMessage());
        verify(airline, never()).suspend();
        verify(airlineRepository, never()).save(any(Airline.class));
    }

    @Test
    void shouldCancelAllFlightsWhenAirlineIsSuspended() {
        Airline airline = mock(Airline.class);
        Flight flight1 = mock(Flight.class);
        Flight flight2 = mock(Flight.class);

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airline.isActive()).thenReturn(true);

        when(flightRepository.findByAirlineAndStatus(airline, FlightStatus.SCHEDULED))
            .thenReturn(List.of(flight1, flight2));
        
        airlineService.suspend(1L);

        verify(airline).suspend();
        verify(flight1).cancel();
        verify(flight2).cancel();
        verify(flightRepository).saveAll(anyList());
        verify(airlineRepository).save(airline);
    }

    @Test
    void shouldNotCancelFlightsWhenNoScheduledWhenAirlineIsDeactivated() {
        Airline airline = mock(Airline.class);

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airline.isActive()).thenReturn(true);

        when(flightRepository.findByAirlineAndStatus(airline, FlightStatus.SCHEDULED)).thenReturn(List.of());

        airlineService.suspend(1L);

        verify(flightRepository).saveAll(List.of());
        verify(airline).suspend();
        verify(airlineRepository).save(airline);
    }
}
