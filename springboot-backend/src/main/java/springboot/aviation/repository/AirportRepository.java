package springboot.aviation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.aviation.model.Airport;


@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    
    boolean existsByIataCode(String iataCode);
    
}
