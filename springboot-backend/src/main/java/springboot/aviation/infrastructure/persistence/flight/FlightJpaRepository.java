package springboot.aviation.infrastructure.persistence.flight;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlightJpaRepository extends JpaRepository<FlightEntity, Long>{
    boolean existsByFlightNumber(String flightNumber);
    Optional<FlightEntity> findByFlightNumber(String flightNumber);
}
