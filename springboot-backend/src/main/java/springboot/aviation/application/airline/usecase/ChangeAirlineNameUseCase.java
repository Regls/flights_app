package springboot.aviation.application.airline.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.airline.Airline;
import springboot.aviation.domain.airline.AirlineRepository;


@Service
public class ChangeAirlineNameUseCase {
    
    private final AirlineRepository airlineRepository;
    
    public ChangeAirlineNameUseCase(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public Airline execute(Long airlineId, String airlineName) {
        Airline airline = airlineRepository.findById(airlineId)
            .orElseThrow();

        airline.changeName(airlineName);
        return airlineRepository.save(airline);
    }
}
