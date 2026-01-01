package springboot.aviation.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.dto.CreateAirlineRequest;
import springboot.aviation.model.Airline;
import springboot.aviation.repository.AirlineRepository;


@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {
    
    @Mock
    private AirlineRepository repository;

    @InjectMocks
    private AirlineService service;

    @Test
    void shouldNotCreateAirlineWithDuplicatedIataCode() {

        when(repository.existsByIataCode("AA")).thenReturn(true);

        CreateAirlineRequest request = new CreateAirlineRequest();
        request.iataCode = "AA";

        assertThrows(BusinessException.class,
            () -> service.createAirline(request));
    }

    @Test
    void shouldSuspendAirlineSuccessfully() {

        Airline airline = Airline.createAirline("American Airlines", "AA");

        when(repository.findById(1L)).thenReturn(Optional.of(airline));
        when(repository.save(airline)).thenReturn(airline);

        service.suspend(1L);

        assertFalse(airline.isActive());
    }
    
}
