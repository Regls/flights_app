package springboot.aviation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.aviation.dto.request.ChangeAirportRequest;
import springboot.aviation.dto.request.CreateAirportRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Airport;
import springboot.aviation.model.Flight;
import springboot.aviation.model.FlightStatus;
import springboot.aviation.repository.AirportRepository;
import springboot.aviation.repository.FlightRepository;


@Service
public class AirportService {
    
    private final AirportRepository airportRepository;

    private final FlightRepository flightRepository;

    public AirportService(AirportRepository airportRepository, FlightRepository flightRepository) {
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
    }

    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    public Airport findById(Long airportId) {
        return airportRepository.findById(airportId)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));
    }

    public Airport createAirport(CreateAirportRequest request) {

        if (airportRepository.existsByIataCode(request.iataCode)) {
            throw new BusinessException("Airport with IATA code already exists");
        }

        Airport airport = Airport.createAirport(
                request.iataCode,
                request.airportName,
                request.city
        );

        return airportRepository.save(airport);
    }

    public Airport changeAirportName(Long airportId, ChangeAirportRequest request) {
        Airport airport = airportRepository.findById(airportId)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));

        airport.changeName(request.airportName);
        return airportRepository.save(airport);
    }

    public void open(Long airportId) {
        Airport airport = airportRepository.findById(airportId)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));

        if(airport.isOperational()) throw new BusinessException("Airport is already open");
        
        airport.open();
        airportRepository.save(airport);
    }

    @Transactional(rollbackFor = BusinessException.class)
    public void close(Long airportId) {
        Airport airport = airportRepository.findById(airportId)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));

        if(!airport.isOperational()) throw new BusinessException("Airport is already closed");

        List<Flight> scheduledFlights = flightRepository.findByDepartureAirportOrArrivalAirportAndStatus(airport, airport, FlightStatus.SCHEDULED);

        for (Flight flight : scheduledFlights) {
            flight.cancel();
        }

        airport.close();
        flightRepository.saveAll(scheduledFlights);
        airportRepository.save(airport);
    }

}
