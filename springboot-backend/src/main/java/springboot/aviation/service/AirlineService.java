package springboot.aviation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.aviation.dto.CreateAirlineRequest;
import springboot.aviation.dto.ChangeAirlineRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Airline;
import springboot.aviation.model.Flight;
import springboot.aviation.model.FlightStatus;
import springboot.aviation.repository.AirlineRepository;
import springboot.aviation.repository.FlightRepository;


@Service
public class AirlineService {
    
    private final AirlineRepository airlineRepository;

    private final FlightRepository flightRepository;

    public AirlineService(AirlineRepository airlineRepository, FlightRepository flightRepository) {
        this.airlineRepository = airlineRepository;
        this.flightRepository = flightRepository;
    }

    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }

    public Airline findById(Long airlineId) {
        return airlineRepository.findById(airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));
    }

    public Airline createAirline(CreateAirlineRequest request) {

        if (airlineRepository.existsByIataCode(request.iataCode)) {
            throw new BusinessException("Airline with IATA code already exists.");
        }

        Airline airline = Airline.createAirline(
                request.airlineName,
                request.iataCode
        );

        return airlineRepository.save(airline);
    }

    public Airline changeAirlineName(Long airlineId, ChangeAirlineRequest request) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));

        airline.changeName(request.airlineName);
        return airlineRepository.save(airline);
    }

    public void activate(Long airlineId) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));

        airline.activate();
        airlineRepository.save(airline);
    }

    @Transactional(rollbackFor = BusinessException.class)
    public void suspend(Long airlineId) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));

        List<Flight> scheduledFlights = flightRepository.findByAirlineAndStatus(airline, FlightStatus.SCHEDULED);

        for (Flight flight : scheduledFlights) {
            flight.cancel();
        }

        airline.suspend();
        flightRepository.saveAll(scheduledFlights);
        airlineRepository.save(airline);
    }
    
}
