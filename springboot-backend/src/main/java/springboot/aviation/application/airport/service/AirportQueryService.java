package springboot.aviation.application.airport.service;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.airport.Airport;
import springboot.aviation.domain.airport.AirportRepository;
import springboot.aviation.exception.ResourceNotFoundException;


@Service
public class AirportQueryService {
    
    private final AirportRepository airportRepository;

    public AirportQueryService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    public Airport findById(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));
    }

    public Airport findByIataCode(String iataCode) {
        return airportRepository.findByIataCode(iataCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));
    }
}
