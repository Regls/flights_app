package springboot.aviation.domain.flight;

import java.util.List;
import java.util.Optional;


public interface FlightRepository {

    List<Flight> findAll();
    
    Optional<Flight> findById(Long id);

    Optional<Flight> findByFlightNumber(String flightNumber);

    Flight save(Flight flight);

    boolean existsByFlightNumber(String flightNumber);
}
