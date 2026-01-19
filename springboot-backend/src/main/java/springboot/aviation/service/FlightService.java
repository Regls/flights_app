package springboot.aviation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.aviation.dto.request.CreateFlightRequest;
import springboot.aviation.dto.response.FlightResponse;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Airline;
import springboot.aviation.model.Airport;
import springboot.aviation.model.Flight;
import springboot.aviation.repository.AirlineRepository;
import springboot.aviation.repository.AirportRepository;
import springboot.aviation.repository.FlightRepository;


@Service
public class FlightService {
    
    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;

    public FlightService(FlightRepository flightRepository, AirlineRepository airlineRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airlineRepository = airlineRepository;
        this.airportRepository = airportRepository;
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Flight findById(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
    }

    public List<FlightResponse> findByAirlineId(Long airlineId) {
        return flightRepository.findByAirlineId(airlineId)
                .stream()
                .map(FlightResponse::from)
                .toList();
    }

    public Flight createFlight(CreateFlightRequest request) {

        Airline airline = airlineRepository.findById(request.airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));

        Airport departure = airportRepository.findById(request.departureAirportId)
                .orElseThrow(() -> new ResourceNotFoundException("Departure airport not found"));

        Airport arrival = airportRepository.findById(request.arrivalAirportId)
                .orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found"));

        if (flightRepository.existsByFlightNumber(request.flightNumber)) {
            throw new BusinessException("Flight with flight number already exists");
        }
        
        Flight flight = Flight.createFlight(
                request.flightNumber,
                airline,
                departure,
                arrival,
                request.departureTime,
                request.arrivalTime
        );

        return flightRepository.save(flight);
    }

    public void depart(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        if(!flight.isScheduled()) throw new BusinessException("Only scheduled flights can depart");

        flight.depart();
        flightRepository.save(flight);
    }

    public void arrive(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        if(!flight.isInFlight()) throw new BusinessException("Only in-flight flights can arrive");

        flight.arrive();
        flightRepository.save(flight);
    }

    @Transactional(rollbackFor = BusinessException.class)
    public void cancel(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        flight.cancel();

        flightRepository.save(flight);
    }
}
