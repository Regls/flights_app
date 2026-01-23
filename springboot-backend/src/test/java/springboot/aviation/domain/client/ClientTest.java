package springboot.aviation.domain.client;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.ClientMessages;
import springboot.aviation.domain.client.Client;


public class ClientTest {
    
    //support methods
    private Client validClient() {
        return Client.create("12345678900", "New", "Client");
    }

    //tests
    @Test
    void shouldCreateClientSucessfully(){
        Client client = validClient();

        assertEquals("12345678900", client.getCpf());
        assertEquals("New", client.getFirstName());
        assertEquals("Client", client.getLastName());
        assertTrue(client.isActive());
    }

    @Test
    void shouldNotCreateClientIfCpfIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create(null, "New", "Client"));

        assertEquals(ClientMessages.CPF_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfCpfIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create("", "New", "Client"));

        assertEquals(ClientMessages.CPF_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfCpfIsNotElevenDigits(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create("123456789", "New", "Client"));

        assertEquals(ClientMessages.CPF_11_DIGITS, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfCpfIsNotNumeric(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create("123456789aa", "New", "Client"));

        assertEquals(ClientMessages.CPF_ONLY_DIGITS, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientFirstNameIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create("12345678900", null, "Client"));

        assertEquals(ClientMessages.FIRST_NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientFirstNameIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create("12345678900", "", "Client"));

        assertEquals(ClientMessages.FIRST_NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientLastNameIsNull(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create("12345678900", "New", null));

        assertEquals(ClientMessages.LAST_NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientLastNameIsMissing(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create("12345678900", "New", ""));

        assertEquals(ClientMessages.LAST_NAME_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientFirstNameIsNotLetters(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create("12345678900", "New1", "Client"));

        assertEquals(ClientMessages.NAME_ONLY_LETTERS, exception.getMessage());
    }

    @Test
    void shouldNotCreateClientIfClientLastNameIsNotLetters(){
        BusinessException exception = assertThrows(BusinessException.class,
            () -> Client.create("12345678900", "New", "Client1"));

        assertEquals(ClientMessages.NAME_ONLY_LETTERS, exception.getMessage());
    }

    @Test
    void shouldChangeClientNameSucessfully(){
        Client client = validClient();

        client.changeName("ChangeNew", "ChangeClient");

        assertEquals("ChangeNew", client.getFirstName());
        assertEquals("ChangeClient", client.getLastName());
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
