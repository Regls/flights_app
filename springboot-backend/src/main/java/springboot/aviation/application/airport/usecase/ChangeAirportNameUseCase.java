package springboot.aviation.application.airport.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.airport.Airport;
import springboot.aviation.domain.airport.AirportRepository;


@Service
public class ChangeAirportNameUseCase {
    
    private final AirportRepository airportRepository;

    public ChangeAirportNameUseCase(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public Airport execute(Long airportId, String airportName) {
        Airport airport = airportRepository.findById(airportId)
            .orElseThrow();
        
        airport.changeName(airportName);
        return airportRepository.save(airport);
    }
}
