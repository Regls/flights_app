package springboot.aviation.infrastructure.persistence.client;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long>{
    boolean existsByCpf(String cpf);
    Optional<ClientEntity> findByCpf(String cpf);
}
