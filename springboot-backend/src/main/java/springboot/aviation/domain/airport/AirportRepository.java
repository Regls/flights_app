package springboot.aviation.domain.airport;

import java.util.List;
import java.util.Optional;


public interface AirportRepository {

    List<Airport> findAll();
    
    Optional<Airport> findById(Long id);

    Optional<Airport> findByIataCode(String iataCode);

    Airport save(Airport airport);

    boolean existsByIataCode(String iataCode);

    boolean existsById(Long id);
}
