package springboot.aviation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.dto.request.ChangeClientRequest;
import springboot.aviation.dto.request.CreateClientRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Booking;
import springboot.aviation.model.BookingStatus;
import springboot.aviation.model.Client;
import springboot.aviation.repository.ClientRepository;
import springboot.aviation.repository.BookingRepository;


@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ClientService clientService;

    //support methods
    private CreateClientRequest validRequest(){
        CreateClientRequest request = new CreateClientRequest();
        request.cpf = "12345678900";
        request.clientFirstName = "New";
        request.clientLastName = "Client";
        return request;
    }

    private void mockValidDependencies(){
        when(clientRepository.existsByCpf("12345678900")).thenReturn(false);
    }

    private ChangeClientRequest validChangeRequest(){
        ChangeClientRequest request = new ChangeClientRequest();
        request.clientFirstName = "Newnew";
        request.clientLastName = "Clientclient";
        return request;
    }

    //tests
    @Test
    void shouldReturnClientList() {

        Client client = mock(Client.class);

        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<Client> clients = clientService.findAll();

        assertEquals(1, clients.size());
        assertTrue(clients.contains(client));
        verify(clientRepository).findAll();
    }

    @Test
    void shouldReturnClientById(){

        Client client = mock(Client.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client clientFound = clientService.findById(1L);

        assertEquals(client, clientFound);
        verify(clientRepository).findById(1L);
    }

    @Test
    void shouldNotReturnClientByIdWhenNonExistingClient(){

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> clientService.findById(1L));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository).findById(1L);
    }

    @Test
    void shouldCreateClientSucessfully(){

        mockValidDependencies();

        when(clientRepository.save(any(Client.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Client client = clientService.createClient(validRequest());

        assertNotNull(client);
        assertTrue(client.isActive());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void shouldNotCreateClientWithDuplicatedCpf() {

        when(clientRepository.existsByCpf("12345678900")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> clientService.createClient(validRequest()));
        
        assertEquals("Client with CPF already exists", exception.getMessage());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void shouldChangeClientNameSucessfully() {

        Client client = mock(Client.class);

        ChangeClientRequest request = validChangeRequest();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.changeClientName(1L, request);

        verify(client).changeName("Newnew", "Clientclient");
        verify(clientRepository).save(client);
    }

    @Test
    void shouldNotChangeClientNameWhenNonExistingClient(){
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> clientService.changeClientName(1L, validChangeRequest()));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void shouldActivateClientSucessfully() {

        Client client = mock(Client.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.activate(1L);

        verify(client).activate();
        verify(clientRepository).save(client);
    }

    @Test
    void shouldNotActivateWhenNonExistingClient() {

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> clientService.activate(1L));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void shouldNotActivateWhenAlreadyActiveClient() {

        Client client = mock(Client.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        when(client.isActive()).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> clientService.activate(1L));

        assertEquals("Client already active", exception.getMessage());
        verify(client, never()).activate();
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void shouldDeactivateClientSucessfully() {

        Client client = mock(Client.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(client.isActive()).thenReturn(true);

        clientService.deactivate(1L);

        verify(client).deactivate();
        verify(clientRepository).save(client);
    }

    @Test
    void shouldNotDeactivateWhenNonExistingClient() {

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> clientService.deactivate(1L));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void shouldNotDeactivateWhenAlreadyInactiveClient() {

        Client client = mock(Client.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        when(client.isActive()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> clientService.deactivate(1L));

        assertEquals("Client already inactive", exception.getMessage());
        verify(client, never()).deactivate();
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void shouldCancelAllBookingsWhenClientIsDeactivated() {
        Client client = mock(Client.class);
        Booking booking1 = mock(Booking.class);
        Booking booking2 = mock(Booking.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(client.isActive()).thenReturn(true);

        when(bookingRepository.findByClientAndStatusIn(client, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED))).thenReturn(List.of(booking1, booking2));

        clientService.deactivate(1L);

        verify(client).deactivate();
        verify(booking1).cancel();
        verify(booking2).cancel();
        verify(bookingRepository).saveAll(anyList());
        verify(clientRepository).save(client);
    }

    @Test
    void shouldNotCancelBookingsWhenNoCreatedOrConfirmedWhenClientIsDeactivated(){
        Client client = mock(Client.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(client.isActive()).thenReturn(true);

        when(bookingRepository.findByClientAndStatusIn(client, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED))).thenReturn(List.of());

        clientService.deactivate(1L);

        verify(bookingRepository).saveAll(List.of());
        verify(client).deactivate();
        verify(clientRepository).save(client);
    }
}
