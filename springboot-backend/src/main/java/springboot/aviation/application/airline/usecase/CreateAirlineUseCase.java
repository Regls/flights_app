package springboot.aviation.application.airline.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.airline.Airline;
import springboot.aviation.domain.airline.AirlineRepository;

import springboot.aviation.exception.BusinessException;


@Service
public class CreateAirlineUseCase {
    
    private final AirlineRepository airlineRepository;
    
    public CreateAirlineUseCase(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public Airline execute(String iataCode, String airlineName) {
        
        if (airlineRepository.existsByIataCode(iataCode)) throw new BusinessException("Airline with IATA code already exists");

        Airline airline = Airline.create(iataCode, airlineName);
        return airlineRepository.save(airline);
    }
}
