package springboot.aviation.infrastructure.persistence.client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long>{
    boolean existsByCpf(String cpf);
}
