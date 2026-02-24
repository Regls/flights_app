package springboot.aviation.infrastructure.persistence.airline;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import springboot.aviation.domain.airline.AirlineStatus;


public interface AirlineJpaRepository extends JpaRepository<AirlineEntity, Long>{
    boolean existsByIataCode(String iataCode);
    List<AirlineEntity> findByStatus(AirlineStatus status);
    Optional<AirlineEntity> findByIataCode(String iataCode);
}
