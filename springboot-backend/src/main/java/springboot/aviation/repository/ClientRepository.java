package springboot.aviation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.aviation.model.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

    boolean existsByCpf(String cpf);
}
