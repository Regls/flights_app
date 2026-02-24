package springboot.aviation.infrastructure.persistence.client;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import springboot.aviation.domain.client.ClientStatus;


public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long>{
    boolean existsByCpf(String cpf);
    List<ClientEntity> findByStatus(ClientStatus status);
    Optional<ClientEntity> findByCpf(String cpf);
}
