package springboot.aviation.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class ClientTest {
    
    //support methods
    private Client validClient() {
        return Client.createClient("12345678900", "New", "Client");
    }

    //tests
    @Test
    void shouldCreateClientSucessfully(){
        Client client = validClient();

        assertTrue(client.hasCpf("12345678900"));
        assertTrue(client.hasFirstName("New"));
        assertTrue(client.hasLastName("Client"));
        assertTrue(client.isActive());
    }

    @Test
    void shouldNotCreateClientIfCpfIsNull(){
        assertThrows(BusinessException.class, () -> Client.createClient(null, "New", "Client"));
    }

    @Test
    void shouldNotCreateClientIfCpfIsMissing(){
        assertThrows(BusinessException.class, () -> Client.createClient("", "New", "Client"));
    }

    @Test
    void shouldNotCreateClientIfCpfIsNotElevenDigits(){
        assertThrows(BusinessException.class, () -> Client.createClient("123456789", "New", "Client"));
    }

    @Test
    void shouldNotCreateClientIfCpfIsNotNumeric(){
        assertThrows(BusinessException.class, () -> Client.createClient("123456789aa", "New", "Client"));
    }

    @Test
    void shouldNotCreateClientIfClientFirstNameIsNull(){
        assertThrows(BusinessException.class, () -> Client.createClient("12345678900", null, "Client"));
    }

    @Test
    void shouldNotCreateClientIfClientFirstNameIsMissing(){
        assertThrows(BusinessException.class, () -> Client.createClient("12345678900", "", "Client"));
    }

    @Test
    void shouldNotCreateClientIfClientLastNameIsNull(){
        assertThrows(BusinessException.class, () -> Client.createClient("12345678900", "New", null));
    }

    @Test
    void shouldNotCreateClientIfClientLastNameIsMissing(){
        assertThrows(BusinessException.class, () -> Client.createClient("12345678900", "New", ""));
    }

    @Test
    void shouldNotCreateClientIfClientFirstNameIsNotLetters(){
        assertThrows(BusinessException.class, () -> Client.createClient("12345678900", "New1", "Client"));
    }

    @Test
    void shouldNotCreateClientIfClientLastNameIsNotLetters(){
        assertThrows(BusinessException.class, () -> Client.createClient("12345678900", "New", "Client1"));
    }

    @Test
    void shouldChangeClientNameSucessfully(){
        Client client = validClient();

        client.changeName("ChangeNew", "ChangeClient");

        assertTrue(client.hasFirstName("ChangeNew"));
        assertTrue(client.hasLastName("ChangeClient"));
        assertTrue(client.isActive());
    }

    @Test
    void shouldNotChangeClientNameIfClientFirstNameIsNull(){
        Client client = validClient();

        assertThrows(BusinessException.class, () -> client.changeName(null, "Client"));
    }

    @Test
    void shouldNotChangeClientNameIfClientFirstNameIsMissing(){
        Client client = validClient();

        assertThrows(BusinessException.class, () -> client.changeName("", "Client"));
    }

    @Test
    void shouldNotChangeClientNameIfClientLastNameIsNull(){
        Client client = validClient();

        assertThrows(BusinessException.class, () -> client.changeName("New", null));
    }

    @Test
    void shouldNotChangeClientNameIfClientLastNameIsMissing(){
        Client client = validClient();

        assertThrows(BusinessException.class, () -> client.changeName("New", ""));
    }

    @Test
    void shouldNotChangeClientNameIfClientFirstNameIsNotLetters(){
        Client client = validClient();

        assertThrows(BusinessException.class, () -> client.changeName("New1", "Client"));
    }

    @Test
    void shouldNotChangeClientNameIfClientLastNameIsNotLetters(){
        Client client = validClient();

        assertThrows(BusinessException.class, () -> client.changeName("New", "Client1"));
    }

    @Test
    void shouldActivateClientSucessfully(){
        Client client = validClient();
        client.deactivate();

        client.activate();

        assertTrue(client.isActive());
    }

    @Test
    void shouldDeactivateClientSucessfully(){
        Client client = validClient();

        client.deactivate();

        assertFalse(client.isActive());
    }

    @Test
    void shouldNotActivateClientIfAlreadyActive(){
        Client client = validClient();

        assertThrows(BusinessException.class, () -> client.activate());
    }

    @Test
    void shouldNotDeactivateClientIfAlreadyInactive(){
        Client client = validClient();
        client.deactivate();

        assertThrows(BusinessException.class, () -> client.deactivate());
    }

}
