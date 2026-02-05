package springboot.aviation.application.flight.service;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.airport.AirportRepository;
import springboot.aviation.domain.airline.AirlineRepository;
import springboot.aviation.domain.flight.Flight;
import springboot.aviation.domain.flight.FlightRepository;
import springboot.aviation.exception.ResourceNotFoundException;


@Service
public class FlightQueryService {
    
    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;

    public FlightQueryService(FlightRepository flightRepository, AirlineRepository airlineRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airlineRepository = airlineRepository;
        this.airportRepository = airportRepository;
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

    public List<Flight> findAllFlightsByAirports(Long airportId) {
        if(!airportRepository.existsById(airportId)) throw new ResourceNotFoundException("Airport not found");
        return flightRepository.findAllFlightsByAirports(airportId);
    }

    public List<Flight> findAllFlightsByAirline(Long airlineId) {
        if(!airlineRepository.existsById(airlineId)) throw new ResourceNotFoundException("Airline not found");
        return flightRepository.findAllFlightsByAirline(airlineId);
    }
}
