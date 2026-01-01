package springboot.aviation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.aviation.model.Airline;


@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
    
    boolean existsByIataCode(String iataCode);

}
