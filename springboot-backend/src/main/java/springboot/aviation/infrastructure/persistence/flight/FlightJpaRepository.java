package springboot.aviation.infrastructure.persistence.flight;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import springboot.aviation.domain.flight.FlightStatus;


public interface FlightJpaRepository extends JpaRepository<FlightEntity, Long>{
    boolean existsByFlightNumber(String flightNumber);
    
    List<FlightEntity> findByDepartureAirportIdAndStatus(Long airportId, FlightStatus status);
    List<FlightEntity> findByArrivalAirportIdAndStatus(Long airportId, FlightStatus status);
    
    Optional<FlightEntity> findByFlightNumber(String flightNumber);
}
