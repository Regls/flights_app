package springboot.aviation.application.airport.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.airport.Airport;
import springboot.aviation.domain.airport.AirportRepository;


@Service
public class OpenAirportUseCase {
    
    private final AirportRepository airportRepository;

    public OpenAirportUseCase(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public void execute(Long airportId) {
        Airport airport = airportRepository.findById(airportId)
            .orElseThrow();
        
        airport.open();
        airportRepository.save(airport);
    }
}
