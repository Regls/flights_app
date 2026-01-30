package springboot.aviation.infrastructure.persistence.airline;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AirlineJpaRepository extends JpaRepository<AirlineEntity, Long>{
    boolean existsByIataCode(String iataCode);
    Optional<AirlineEntity> findByIataCode(String iataCode);
}
