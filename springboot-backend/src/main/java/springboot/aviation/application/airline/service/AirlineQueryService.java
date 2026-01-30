package springboot.aviation.application.airline.service;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.airline.Airline;
import springboot.aviation.domain.airline.AirlineRepository;
import springboot.aviation.exception.ResourceNotFoundException;


@Service
public class AirlineQueryService {
    
    private final AirlineRepository airlineRepository;

    public AirlineQueryService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }

    public Airline findById(Long id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));
    }

    public Airline findByIataCode(String iataCode) {
        return airlineRepository.findByIataCode(iataCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));
    }
}
