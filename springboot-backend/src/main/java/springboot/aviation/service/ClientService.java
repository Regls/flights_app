package springboot.aviation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.aviation.dto.CreateClientRequest;
import springboot.aviation.dto.ChangeClientRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Booking;
import springboot.aviation.model.BookingStatus;
import springboot.aviation.model.Client;
import springboot.aviation.repository.ClientRepository;
import springboot.aviation.repository.BookingRepository;


@Service
public class ClientService {
    
    private final ClientRepository clientRepository;

    private final BookingRepository bookingRepository;

    public ClientService(ClientRepository clientRepository, BookingRepository bookingRepository) {
        this.clientRepository = clientRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    public Client createClient(CreateClientRequest request) {

        if (clientRepository.existsByCpf(request.cpf)) {
            throw new BusinessException("Client with CPF already exists.");
        }

        Client client = Client.createClient(
                request.cpf,
                request.clientFirstName,
                request.clientLastName
        );

        return clientRepository.save(client);
    }

    public Client changeClientName(Long clientId, ChangeClientRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        client.changeName(request.clientFirstName, request.clientLastName);
        return clientRepository.save(client);
    }

    public void activate(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        client.activate();
        clientRepository.save(client);
    }

    @Transactional(rollbackFor = BusinessException.class)
    public void deactivate(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        List<Booking> bookings = bookingRepository.findByClientAndStatusIn(client, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED));

        for (Booking booking : bookings) {
            booking.cancel();
        }

        client.deactivate();

        bookingRepository.saveAll(bookings);
        clientRepository.save(client);
    }
}

