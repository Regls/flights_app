package springboot.aviation.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.AirlineMessages;

@ExtendWith(MockitoExtension.class)
public class AirlineTest {
    
    //support methods
    private Airline validAirline() {
        return Airline.createAirline("G3", "Gol Airlines");
    }

    //tests
    @Test
    void shouldCreateAirlineSucessfully() {
        Airline airline = validAirline();

        assertTrue(airline.hasIataCode("G3"));
        assertTrue(airline.hasName("Gol Airlines"));
        assertTrue(airline.isActive());
    }

    @Test
    void shouldNotCreateAirlineIfIataCodeIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.createAirline(null, "Gol Airlines"));

        assertEquals(AirlineMessages.IATA_CODE_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfIataCodeIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.createAirline("", "Gol Airlines"));

        assertEquals(AirlineMessages.IATA_CODE_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfIataCodeIsNotTwoDigits(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.createAirline("G", "Gol Airlines"));

        assertEquals(AirlineMessages.IATA_CODE_INVALID, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.createAirline("G3", null));

        assertEquals(AirlineMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.createAirline("G3", ""));

        assertEquals(AirlineMessages.NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameIsNotLetters(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.createAirline("G3", "Gol Airlines 123"));
            
        assertEquals(AirlineMessages.NAME_ONLY_LETTERS_AND_SINGLE_SPACES, exception.getMessage());
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameHasDoubleSpaces(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Airline.createAirline("G3", "Gol  Airlines"));

        assertEquals(AirlineMessages.NAME_ONLY_LETTERS_AND_SINGLE_SPACES, exception.getMessage());
    }

    @Test
    void shouldChangeAirlineNameSucessfully(){
        Airline airline = validAirline();

        airline.changeName("Azul Airlines");

        assertTrue(airline.hasName("Azul Airlines"));
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
