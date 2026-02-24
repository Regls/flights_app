package springboot.aviation.infrastructure.persistence.airport;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import springboot.aviation.domain.airport.AirportStatus;


public interface AirportJpaRepository extends JpaRepository<AirportEntity, Long>{
    boolean existsByIataCode(String iataCode);
    List<AirportEntity> findByStatus(AirportStatus status);
    Optional<AirportEntity> findByIataCode(String iataCode);
}
