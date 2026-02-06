package springboot.aviation.domain.client;

import java.util.List;
import java.util.Optional;


public interface ClientRepository {

    List<Client> findAll();
    
    Optional<Client> findById(Long id);

    Optional<Client> findByCpf(String cpf);

    Client save(Client client);

    boolean existsByCpf(String cpf);

    boolean existsById(Long id);
}
