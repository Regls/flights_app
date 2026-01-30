package springboot.aviation.domain.airline;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.AirlineMessages;


public class AirlineTest {
    
    //support methods
    private Airline validAirline() {
        return Airline.create("G3", "Gol Airlines");
    }

    //tests
    @Test
    void shouldCreateAirlineSucessfully() {
        Airline airline = validAirline();

        assertEquals("G3", airline.getIataCode());
        assertEquals("Gol Airlines", airline.getAirlineName());
        assertTrue(airline.isActive());
    }

    @Test
    void shouldNotCreateAirlineIfIataCodeIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.create(null, "Gol Airlines"));

        assertEquals(AirlineMessages.IATA_CODE_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfIataCodeIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.create("", "Gol Airlines"));

        assertEquals(AirlineMessages.IATA_CODE_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfIataCodeIsNotTwoDigits(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.create("G", "Gol Airlines"));

        assertEquals(AirlineMessages.IATA_CODE_INVALID, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.create("G3", null));

        assertEquals(AirlineMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.create("G3", ""));

        assertEquals(AirlineMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameIsNotLetters(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.create("G3", "Gol Airlines 123"));
            
        assertEquals(AirlineMessages.NAME_ONLY_LETTERS_AND_SINGLE_SPACES, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameHasDoubleSpaces(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.create("G3", "Gol  Airlines"));

        assertEquals(AirlineMessages.NAME_ONLY_LETTERS_AND_SINGLE_SPACES, exception.getMessage());
    }

    @Test
    void shouldChangeAirlineNameSucessfully(){
        Airline airline = validAirline();

        airline.changeName("Azul Airlines");

        assertEquals("Azul Airlines", airline.getAirlineName());
        assertTrue(airline.isActive());
    }

    @Test
    void shouldNotChangeAirlineNameIfAirlineNameIsNull(){
        Airline airline = validAirline();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airline.changeName(null));

        assertEquals(AirlineMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotChangeAirlineNameIfAirlineNameIsMissing(){
        Airline airline = validAirline();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airline.changeName(""));

        assertEquals(AirlineMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotChangeAirlineNameIfAirlineNameIsNotLetters(){
        Airline airline = validAirline();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airline.changeName("Azul Airlines 123"));

        assertEquals(AirlineMessages.NAME_ONLY_LETTERS_AND_SINGLE_SPACES, exception.getMessage());
    }

    @Test
    void shouldNotChangeAirlineNameIfAirlineNameHasDoubleSpaces(){
        Airline airline = validAirline();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airline.changeName("Azul  Airlines"));

        assertEquals(AirlineMessages.NAME_ONLY_LETTERS_AND_SINGLE_SPACES, exception.getMessage());
    }

    @Test
    void shouldActivateAirlineSucessfully(){
        Airline airline = validAirline();
        airline.suspend();

        airline.activate();

        assertTrue(airline.isActive());
    }

    @Test
    void shouldSuspendAirlineSucessfully(){
        Airline airline = validAirline();

        airline.suspend();

        assertFalse(airline.isActive());
    }

    @Test
    void shouldNotActivateAirlineIfAlreadyActive(){
        Airline airline = validAirline();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airline.activate());

        assertEquals(AirlineMessages.AIRLINE_ALREADY_ACTIVE, exception.getMessage());
    }

    @Test
    void shouldNotSuspendAirlineIfAlreadySuspended(){
        Airline airline = validAirline();
        airline.suspend();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> airline.suspend());

        assertEquals(AirlineMessages.AIRLINE_ALREADY_SUSPENDED, exception.getMessage());
    }
}
