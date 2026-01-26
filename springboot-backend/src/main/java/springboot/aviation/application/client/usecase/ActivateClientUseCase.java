package springboot.aviation.application.client.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.client.ClientRepository;


@Service
public class ActivateClientUseCase {
    
    private final ClientRepository clientRepository;

    public ActivateClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow();

        client.activate();
        clientRepository.save(client);
    }
}
