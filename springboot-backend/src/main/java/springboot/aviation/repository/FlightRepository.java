/* package springboot.aviation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.aviation.model.Flight;
import springboot.aviation.model.FlightStatus;
import springboot.aviation.model.Airline;
import springboot.aviation.model.Airport;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumber(String flightNumber);

    List<Flight> findByAirlineAndStatus(Airline airline, FlightStatus status);

    List<Flight> findByDepartureAirportOrArrivalAirportAndStatus(Airport departureAirport, Airport arrivalAirport, FlightStatus status);

    List<Flight> findByAirlineId(Long airlineId);

    List<Flight> findByDepartureAirportId(Long airportId);
    
    List<Flight> findByArrivalAirportId(Long airportId);
}
 */