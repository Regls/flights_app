package springboot.aviation.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class AirportTest {
    
    //support methods
    private Airport validAirport() {
        return Airport.createAirport("GRU", "Guarulhos International Airport", "São Paulo");
    }

    private static final String REQUIRED = " is required";

    //tests
    @Test
    void shouldCreateAirportSucessfully(){
        Airport airport = validAirport();

        assertTrue(airport.hasIataCode("GRU"));
        assertTrue(airport.hasName("Guarulhos International Airport"));
        assertTrue(airport.isIsCity("São Paulo"));
        assertTrue(airport.isOperational());
    }

    @Test
    void shouldNotCreateAirportIfIataCodeIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.createAirport(null, "Guarulhos International Airport", "São Paulo"));

        assertEquals("IATA code"+REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfIataCodeIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.createAirport(null, "Guarulhos International Airport", "São Paulo"));

        assertEquals("IATA code"+REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfIataCodeIsNotThreeDigits(){
        assertThrows(BusinessException.class, () -> Airport.createAirport("GR", "Guarulhos International Airport", "São Paulo"));
    }

    @Test
    void shouldNotCreateAirportIfIataCodeIsNotLetters(){
        assertThrows(BusinessException.class, () -> Airport.createAirport("GR1", "Guarulhos International Airport", "São Paulo"));
    }

    @Test
    void shouldNotCreateAirportIfAirportNameIsNull(){
        assertThrows(BusinessException.class, () -> Airport.createAirport("GRU", null, "São Paulo"));
    }

    @Test
    void shouldNotCreateAirportIfAirportNameIsMissing(){
        assertThrows(BusinessException.class, () -> Airport.createAirport("GRU", "", "São Paulo"));
    }

    @Test
    void shouldNotCreateAirportIfAirportNameIsNotLetters(){
        assertThrows(BusinessException.class, () -> Airport.createAirport("GRU", "Guarulhos International Airport 1", "São Paulo"));
    }

    @Test
    void shouldNotCreateAirportIfCityIsNull(){
        assertThrows(BusinessException.class, () -> Airport.createAirport("GRU", "Guarulhos International Airport", null));
    }

    @Test
    void shouldNotCreateAirportIfCityIsMissing(){
        assertThrows(BusinessException.class, () -> Airport.createAirport("GRU", "Guarulhos International Airport", ""));
    }

    @Test
    void shouldNotCreateAirportIfCityIsNotLetters(){
        assertThrows(BusinessException.class, () -> Airport.createAirport("GRU", "Guarulhos International Airport", "São Paulo 1"));
    }

    @Test
    void shouldChangeAirportNameSucessfully(){
        Airport airport = validAirport();

        airport.changeName("Guarulhos National Airport");

        assertTrue(airport.isOperational());
        assertTrue(airport.hasName("Guarulhos National Airport"));
    }

    @Test
    void shouldNotChangeAirportNameIfAirportNameIsNull(){
        Airport airport = validAirport();

        assertThrows(BusinessException.class, () -> airport.changeName(null));
    }

    @Test
    void shouldNotChangeAirportNameIfAirportNameIsMissing(){
        Airport airport = validAirport();

        assertThrows(BusinessException.class, () -> airport.changeName(""));
    }

    @Test
    void shouldNotChangeAirportNameIfAirportNameIsNotLetters(){
        Airport airport = validAirport();

        assertThrows(BusinessException.class, () -> airport.changeName("Guarulhos International Airport 1"));
    }

    @Test
    void shouldOpenAirportSucessfully(){
        Airport airport = validAirport();
        airport.closeAirport();

        airport.openAirport();

        assertTrue(airport.isOperational());
    }

    @Test
    void shouldCloseAirportSucessfully(){
        Airport airport = validAirport();

        airport.closeAirport();

        assertTrue(!airport.isOperational());
    }

    @Test
    void shouldNotOpenAirportIfAirportIsAlreadyOpen(){
        Airport airport = validAirport();

        assertThrows(BusinessException.class, () -> airport.openAirport());
    }

    @Test
    void shouldNotCloseAirportIfAirportIsAlreadyClosed(){
        Airport airport = validAirport();

        airport.closeAirport();

        assertThrows(BusinessException.class, () -> airport.closeAirport());
    }
}
