package springboot.aviation.application.client.service;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.client.ClientRepository;
import springboot.aviation.exception.ResourceNotFoundException;


@Service
public class ClientQueryService {
    
    private final ClientRepository clientRepository;

    public ClientQueryService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    public Client findByCpf(String cpf) {
        return clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }
}
