package springboot.aviation.domain.flight;

import java.util.List;
import java.util.Optional;


public interface FlightRepository {

    List<Flight> findAll();
    
    Optional<Flight> findById(Long id);

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findAllFlightsByAirports(Long airportId);

    List<Flight> findAllFlightsByAirline(Long airlineId);

    List<Flight> findScheduledFlightsByAirports(Long airportId);

    List<Flight> findScheduledFlightsByAirline(Long airlineId);

    Flight save(Flight flight);

    boolean existsByFlightNumber(String flightNumber);

    boolean existsById(Long id);
}
