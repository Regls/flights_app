package springboot.aviation.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.dto.CreateAirportRequest;
import springboot.aviation.model.Airport;
import springboot.aviation.repository.AirportRepository;


@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    private AirportRepository repository;

    @InjectMocks
    private AirportService service;

    @Test
    void shouldNotCreateAirportWithDuplicatedIataCode() {

        when(repository.existsByIataCode("GRU")).thenReturn(true);

        CreateAirportRequest request = new CreateAirportRequest();
        request.iataCode = "GRU";

        assertThrows(BusinessException.class,
            () -> service.createAirport(request));
    }

    @Test
    void shouldCloseAirportSuccessfully() {

        Airport airport = Airport.createAirport("GRU", "Guarulhos International Airport", "São Paulo");

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(airport));
        when(repository.save(airport)).thenReturn(airport);

        service.close(1L);

        assertFalse(airport.isOperational());
    }
    
}
