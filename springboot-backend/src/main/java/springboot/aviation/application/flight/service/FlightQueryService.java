package springboot.aviation.application.flight.service;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.flight.Flight;
import springboot.aviation.domain.flight.FlightRepository;
import springboot.aviation.exception.ResourceNotFoundException;


@Service
public class FlightQueryService {
    
    private final FlightRepository flightRepository;

    public FlightQueryService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Flight findById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
    }

    public Flight findByFlightNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
    }
}
