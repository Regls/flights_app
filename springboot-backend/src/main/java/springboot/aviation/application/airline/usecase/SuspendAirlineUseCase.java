package springboot.aviation.application.airline.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.airline.Airline;
import springboot.aviation.domain.airline.AirlineRepository;


@Service
public class SuspendAirlineUseCase {
    
    private final AirlineRepository airlineRepository;
    
    public SuspendAirlineUseCase(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public Airline execute(Long airlineId) {
        Airline airline = airlineRepository.findById(airlineId)
            .orElseThrow();

        airline.suspend();
        return airlineRepository.save(airline);
    }
}
