package springboot.aviation.infrastructure.persistence.airport;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AirportJpaRepository extends JpaRepository<AirportEntity, Long>{
    boolean existsByIataCode(String iataCode);
    Optional<AirportEntity> findByIataCode(String iataCode);
}
