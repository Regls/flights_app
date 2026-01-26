package springboot.aviation.application.airport.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.airport.Airport;
import springboot.aviation.domain.airport.AirportRepository;

import springboot.aviation.exception.BusinessException;


@Service
public class CreateAirportUseCase {
    
    private final AirportRepository airportRepository;
    
    public CreateAirportUseCase(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public Airport execute(String iataCode, String airportName, String city) {

        if (airportRepository.existsByIataCode(iataCode)) throw new BusinessException("Airport with IATA code already exists");
        
        Airport airport = Airport.create(iataCode, airportName, city);
        return airportRepository.save(airport);
    }
}
