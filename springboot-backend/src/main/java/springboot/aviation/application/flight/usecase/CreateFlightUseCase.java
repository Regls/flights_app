package springboot.aviation.application.flight.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.flight.Flight;
import springboot.aviation.domain.flight.FlightRepository;

import springboot.aviation.domain.airline.Airline;
import springboot.aviation.domain.airline.AirlineRepository;
import springboot.aviation.domain.airport.Airport;
import springboot.aviation.domain.airport.AirportRepository;

import springboot.aviation.exception.BusinessException;


@Service
public class CreateFlightUseCase {
    
    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    
    public CreateFlightUseCase(FlightRepository flightRepository, AirlineRepository airlineRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airlineRepository = airlineRepository;
        this.airportRepository = airportRepository;
    }

    public Flight execute(String flightNumber, Long airlineId, Long departureAirportId, 
                        Long arrivalAirportId, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        
        if (flightRepository.existsByFlightNumber(flightNumber)) 
            throw new BusinessException("Flight already exists");

        Airline airline = airlineRepository.findById(airlineId)
            .orElseThrow(() -> new BusinessException("Airline not found"));

        Airport departure = airportRepository.findById(departureAirportId)
            .orElseThrow(() -> new BusinessException("Departure airport not found"));

        Airport arrival = airportRepository.findById(arrivalAirportId)
            .orElseThrow(() -> new BusinessException("Arrival airport not found"));

        Flight flight = Flight.create(flightNumber, airline, departure, arrival, departureTime, arrivalTime);
        return flightRepository.save(flight);
    }
}
