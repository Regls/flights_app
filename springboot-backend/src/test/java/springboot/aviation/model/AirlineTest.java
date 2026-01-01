package springboot.aviation.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;

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
        assertThrows(BusinessException.class, () -> Airline.createAirline(null, "Gol Airlines"));
    }

    @Test
    void shouldNotCreateAirlineIfIataCodeIsMissing(){
        assertThrows(BusinessException.class, () -> Airline.createAirline("", "Gol Airlines"));
    }

    @Test
    void shouldNotCreateAirlineIfIataCodeIsNotTwoDigits(){
        assertThrows(BusinessException.class, () -> Airline.createAirline("G", "Gol Airlines"));
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameIsNull(){
        assertThrows(BusinessException.class, () -> Airline.createAirline("G3", null));
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameIsMissing(){
        assertThrows(BusinessException.class, () -> Airline.createAirline("G3", ""));
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameIsNotLetters(){
        assertThrows(BusinessException.class, () -> Airline.createAirline("G3", "Gol Airlines 123"));
    }

    @Test
    void shouldNotCreateAirlineIfAirlineNameHasDoubleSpaces(){
        assertThrows(BusinessException.class, () -> Airline.createAirline("G3", "Gol  Airlines"));
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

        assertThrows(BusinessException.class, () -> airline.changeName(null));
    }

    @Test
    void shouldNotChangeAirlineNameIfAirlineNameIsMissing(){
        Airline airline = validAirline();

        assertThrows(BusinessException.class, () -> airline.changeName(""));
    }

    @Test
    void shouldNotChangeAirlineNameIfAirlineNameIsNotLetters(){
        Airline airline = validAirline();

        assertThrows(BusinessException.class, () -> airline.changeName("Azul Airlines 123"));
    }

    @Test
    void shouldNotChangeAirlineNameIfAirlineNameHasDoubleSpaces(){
        Airline airline = validAirline();

        assertThrows(BusinessException.class, () -> airline.changeName("Azul  Airlines"));
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

        assertThrows(BusinessException.class, () -> airline.activate());
    }

    @Test
    void shouldNotSuspendAirlineIfAlreadySuspended(){
        Airline airline = validAirline();
        airline.suspend();

        assertThrows(BusinessException.class, () -> airline.suspend());
    }
}
