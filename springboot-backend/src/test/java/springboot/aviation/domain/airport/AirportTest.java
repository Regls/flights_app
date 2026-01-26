package springboot.aviation.domain.airport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.AirportMessages;


public class AirportTest {
    
    //support methods
    private Airport validAirport() {
        return Airport.create("GRU", "Guarulhos International Airport", "São Paulo");
    }

    //tests
    @Test
    void shouldCreateAirportSucessfully(){
        Airport airport = validAirport();

        assertEquals("GRU", airport.getIataCode());
        assertEquals("Guarulhos International Airport", airport.getAirportName());
        assertEquals("São Paulo", airport.getCity());
        assertTrue(airport.isOpen());
    }

    @Test
    void shouldNotCreateAirportIfIataCodeIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create(null, "Guarulhos International Airport", "São Paulo"));

        assertEquals(AirportMessages.IATA_CODE_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfIataCodeIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create("", "Guarulhos International Airport", "São Paulo"));

        assertEquals(AirportMessages.IATA_CODE_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfIataCodeIsNotThreeDigits(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create("GR", "Guarulhos International Airport", "São Paulo"));

        assertEquals(AirportMessages.IATA_CODE_3_DIGITS, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfIataCodeIsNotLetters(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create("GR1", "Guarulhos International Airport", "São Paulo"));
        
        assertEquals(AirportMessages.IATA_CODE_ONLY_LETTERS, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfAirportNameIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create("GRU", null, "São Paulo"));
        
        assertEquals(AirportMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfAirportNameIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create("GRU", "", "São Paulo"));
        
        assertEquals(AirportMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfAirportNameIsNotLetters(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create("GRU", "Guarulhos International Airport 1", "São Paulo"));
        
        assertEquals(AirportMessages.NAME_ONLY_LETTERS_AND_SPACES, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfCityIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create("GRU", "Guarulhos International Airport", null));

        assertEquals(AirportMessages.CITY_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfCityIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create("GRU", "Guarulhos International Airport", ""));

        assertEquals(AirportMessages.CITY_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirportIfCityIsNotLetters(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airport.create("GRU", "Guarulhos International Airport", "São Paulo 1"));

        assertEquals(AirportMessages.CITY_ONLY_LETTERS_AND_SPACES, exception.getMessage());
    }

    @Test
    void shouldChangeAirportNameSucessfully(){
        Airport airport = validAirport();

        airport.changeName("Guarulhos National Airport");

        assertEquals("Guarulhos National Airport", airport.getAirportName());
        assertTrue(airport.isOpen());
    }

    @Test
    void shouldNotChangeAirportNameIfAirportNameIsNull(){
        Airport airport = validAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airport.changeName(null));

        assertEquals(AirportMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotChangeAirportNameIfAirportNameIsMissing(){
        Airport airport = validAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airport.changeName(""));

        assertEquals(AirportMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotChangeAirportNameIfAirportNameIsNotLetters(){
        Airport airport = validAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airport.changeName("Guarulhos International Airport 1"));

        assertEquals(AirportMessages.NAME_ONLY_LETTERS_AND_SPACES, exception.getMessage());
    }

    @Test
    void shouldOpenAirportSucessfully(){
        Airport airport = validAirport();
        airport.close();

        airport.open();

        assertTrue(airport.isOpen());
    }

    @Test
    void shouldCloseAirportSucessfully(){
        Airport airport = validAirport();

        airport.close();

        assertFalse(airport.isOpen());
    }

    @Test
    void shouldNotOpenAirportIfAirportIsAlreadyOpen(){
        Airport airport = validAirport();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airport.open());

        assertEquals(AirportMessages.AIRPORT_ALREADY_OPEN, exception.getMessage());
    }

    @Test
    void shouldNotCloseAirportIfAirportIsAlreadyClosed(){
        Airport airport = validAirport();

        airport.close();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airport.close());

        assertEquals(AirportMessages.AIRPORT_ALREADY_CLOSED, exception.getMessage());
    }
}
