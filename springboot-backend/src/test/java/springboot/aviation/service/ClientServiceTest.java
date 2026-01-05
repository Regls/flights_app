package springboot.aviation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.dto.ChangeClientRequest;
import springboot.aviation.dto.CreateClientRequest;
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
    }

    @Test
    void shouldReturnClientById(){

        Client client = mock(Client.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client clientFound = clientService.findById(1L);

        assertEquals(client, clientFound);
    }

    @Test
    void shouldCreateClientSucessfully(){

        mockValidDependencies();

        when(clientRepository.save(any(Client.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Client client = clientService.createClient(validRequest());

        assertNotNull(client);
        assertTrue(client.isActive());
    }

    @Test
    void shouldNotCreateClientWithDuplicatedCpf() {

        when(clientRepository.existsByCpf("12345678900")).thenReturn(true);

        assertThrows(BusinessException.class,
            () -> clientService.createClient(validRequest()));
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

        assertThrows(ResourceNotFoundException.class,
            () -> clientService.changeClientName(1L, validChangeRequest()));
    }

    @Test
    void shouldNotActivateWhenNonExistingClient() {

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> clientService.activate(1L));
    }

    @Test
    void shouldNotDeactivateWhenNonExistingClient() {

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> clientService.deactivate(1L));
    }

    @Test
    void shouldCancelAllBookingsWhenClientIsDeactivated() {
        Client client = mock(Client.class);
        Booking booking1 = mock(Booking.class);
        Booking booking2 = mock(Booking.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        when(bookingRepository.findByClientAndStatusIn(client, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED))).thenReturn(List.of(booking1, booking2));

        clientService.deactivate(1L);

        verify(client).deactivate();
        verify(booking1).cancel();
        verify(booking2).cancel();
        verify(bookingRepository).saveAll(anyList());
        verify(clientRepository).save(client);
    }

    @Test
    void shouldNotCancelCancelledBookingsWhenClientIsDeactivated(){
        Client client = mock(Client.class);
        Booking cancelledBooking = mock(Booking.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        when(bookingRepository.findByClientAndStatusIn(any(), any())).thenReturn(List.of());

        clientService.deactivate(1L);

        verify(cancelledBooking, never()).cancel();
    }
}
