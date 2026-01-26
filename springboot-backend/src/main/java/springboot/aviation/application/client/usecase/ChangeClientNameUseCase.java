package springboot.aviation.application.client.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.client.ClientRepository;


@Service
public class ChangeClientNameUseCase {
    
    private final ClientRepository clientRepository;

    public ChangeClientNameUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client execute(Long clientId, String firstName, String lastName) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow();

        client.changeName(firstName, lastName);
        return clientRepository.save(client);
    }
}
