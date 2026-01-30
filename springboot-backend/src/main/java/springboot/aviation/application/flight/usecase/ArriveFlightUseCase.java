package springboot.aviation.application.flight.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.flight.Flight;
import springboot.aviation.domain.flight.FlightRepository;


@Service
public class ArriveFlightUseCase {
    
    private final FlightRepository flightRepository;

    public ArriveFlightUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flight execute(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        flight.arrive();
        return flightRepository.save(flight);
    }
}
