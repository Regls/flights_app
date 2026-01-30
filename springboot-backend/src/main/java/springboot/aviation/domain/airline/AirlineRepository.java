package springboot.aviation.domain.airline;

import java.util.List;
import java.util.Optional;


public interface AirlineRepository {
    
    List<Airline> findAll();
    
    Optional<Airline> findById(Long id);

    Optional<Airline> findByIataCode(String iataCode);

    Airline save(Airline airline);

    boolean existsByIataCode(String iataCode);
}
