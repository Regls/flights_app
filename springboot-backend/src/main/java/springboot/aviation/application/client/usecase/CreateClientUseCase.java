package springboot.aviation.application.client.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.client.ClientRepository;

import springboot.aviation.exception.BusinessException;


@Service
public class CreateClientUseCase {
    
    private final ClientRepository clientRepository;

    public CreateClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client execute(String cpf, String firstName, String lastName) {

        if (clientRepository.existsByCpf(cpf)) throw new BusinessException("Client with CPF already exists");

        Client client = Client.create(cpf, firstName, lastName);
        return clientRepository.save(client);
    }
}
