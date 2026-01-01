package springboot.aviation.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.ClientMessages;

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
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient(null, "New", "Client"));

        assertEquals(ClientMessages.CPF_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfCpfIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient("", "New", "Client"));

        assertEquals(ClientMessages.CPF_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfCpfIsNotElevenDigits(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient("123456789", "New", "Client"));

        assertEquals(ClientMessages.CPF_11_DIGITS, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfCpfIsNotNumeric(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient("123456789aa", "New", "Client"));

        assertEquals(ClientMessages.CPF_ONLY_DIGITS, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientFirstNameIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient("12345678900", null, "Client"));

        assertEquals(ClientMessages.FIRST_NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientFirstNameIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient("12345678900", "", "Client"));

        assertEquals(ClientMessages.FIRST_NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientLastNameIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient("12345678900", "New", null));

        assertEquals(ClientMessages.LAST_NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientLastNameIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient("12345678900", "New", ""));

        assertEquals(ClientMessages.LAST_NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientFirstNameIsNotLetters(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient("12345678900", "New1", "Client"));

        assertEquals(ClientMessages.NAME_ONLY_LETTERS, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientLastNameIsNotLetters(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.createClient("12345678900", "New", "Client1"));

        assertEquals(ClientMessages.NAME_ONLY_LETTERS, exception.getMessage());
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

        BusinessException exception = assertThrows(BusinessException.class,
            () -> client.changeName(null, "Client"));
            
        assertEquals(ClientMessages.FIRST_NAME_REQUIRED, exception.getMessage());

            
    }

    @Test
    void shouldNotChangeClientNameIfClientFirstNameIsMissing(){
        Client client = validClient();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> client.changeName("", "Client"));
            
        assertEquals(ClientMessages.FIRST_NAME_REQUIRED, exception.getMessage());

        
    }

    @Test
    void shouldNotChangeClientNameIfClientLastNameIsNull(){
        Client client = validClient();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> client.changeName("New", null));
            
        assertEquals(ClientMessages.LAST_NAME_REQUIRED, exception.getMessage());

        
    }

    @Test
    void shouldNotChangeClientNameIfClientLastNameIsMissing(){
        Client client = validClient();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> client.changeName("New", ""));
            
        assertEquals(ClientMessages.LAST_NAME_REQUIRED, exception.getMessage());

        
    }

    @Test
    void shouldNotChangeClientNameIfClientFirstNameIsNotLetters(){
        Client client = validClient();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> client.changeName("New1", "Client"));
            
        assertEquals(ClientMessages.NAME_ONLY_LETTERS, exception.getMessage());

        
    }

    @Test
    void shouldNotChangeClientNameIfClientLastNameIsNotLetters(){
        Client client = validClient();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> client.changeName("New", "Client1"));
            
        assertEquals(ClientMessages.NAME_ONLY_LETTERS, exception.getMessage());

        
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

        BusinessException exception = assertThrows(BusinessException.class,
            () -> client.activate());

        assertEquals(ClientMessages.CLIENT_ALREADY_ACTIVE, exception.getMessage());
    }

    @Test
    void shouldNotDeactivateClientIfAlreadyInactive(){
        Client client = validClient();
        client.deactivate();

        BusinessException exception = assertThrows(BusinessException.class,
            () -> client.deactivate());

        assertEquals(ClientMessages.CLIENT_ALREADY_INACTIVE, exception.getMessage());
    }

}
