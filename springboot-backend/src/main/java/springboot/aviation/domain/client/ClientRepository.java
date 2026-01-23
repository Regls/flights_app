package springboot.aviation.domain.client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    List<Client> findAll();
    
    Optional<Client> findById(Long id);

    Client save(Client client);

    boolean existsByCpf(String cpf);
}
