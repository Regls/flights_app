package springboot.aviation.infrastructure.persistence.flight;

import java.util.Optional;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import springboot.aviation.domain.flight.Flight;
import springboot.aviation.infrastructure.mapper.FlightMapper;
import springboot.aviation.infrastructure.persistence.airline.AirlineJpaRepository;
import springboot.aviation.infrastructure.persistence.airport.AirportJpaRepository;
import springboot.aviation.domain.flight.FlightRepository;
import springboot.aviation.domain.flight.FlightStatus;


@Repository
public class FlightRepositoryImpl implements FlightRepository{

    private final FlightJpaRepository flightJpaRepository;
    private final AirlineJpaRepository airlineJpaRepository;
    private final AirportJpaRepository airportJpaRepository;

    public FlightRepositoryImpl(FlightJpaRepository flightJpaRepository,
            AirlineJpaRepository airlineJpaRepository,
            AirportJpaRepository airportJpaRepository
    ) {
        this.flightJpaRepository = flightJpaRepository;
        this.airlineJpaRepository = airlineJpaRepository;
        this.airportJpaRepository = airportJpaRepository;
    }
    
    @Override
    public List<Flight> findAll() {
        return flightJpaRepository.findAll()
                .stream()
                .map(FlightMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return flightJpaRepository.findById(id)
                .map(FlightMapper::toDomain);
    }

    @Override
    public Optional<Flight> findByFlightNumber(String flightNumber) {
        return flightJpaRepository.findByFlightNumber(flightNumber)
                .map(FlightMapper::toDomain);
    }
    
    @Override
    public List<Flight> findAllFlightsByAirports(Long airportId) {
        List<FlightEntity> departureFlights = flightJpaRepository.findByDepartureAirportId(airportId);
        List<FlightEntity> arrivalFlights = flightJpaRepository.findByArrivalAirportId(airportId);

        return Stream.concat(departureFlights.stream(), arrivalFlights.stream())
                .distinct()
                .map(FlightMapper::toDomain)
                .toList();
    }

    @Override
    public List<Flight> findScheduledFlightsByAirports(Long airportId) {
        List<FlightEntity> departureFlights = flightJpaRepository.findByDepartureAirportIdAndStatus(airportId, FlightStatus.SCHEDULED);
        List<FlightEntity> arrivalFlights = flightJpaRepository.findByArrivalAirportIdAndStatus(airportId, FlightStatus.SCHEDULED);
        
        return Stream.concat(departureFlights.stream(), arrivalFlights.stream())
                .distinct()
                .map(FlightMapper::toDomain)
                .toList();
    }

    @Override
    public List<Flight> findAllFlightsByAirline(Long airlineId) {
        return flightJpaRepository.findByAirlineId(airlineId)
                .stream()
                .map(FlightMapper::toDomain)
                .toList();
    }

    @Override
    public List<Flight> findScheduledFlightsByAirline(Long airlineId) {
        return flightJpaRepository.findByAirlineIdAndStatus(airlineId, FlightStatus.SCHEDULED)
                .stream()
                .map(FlightMapper::toDomain)
                .toList();
    }

    @Override
    public Flight save(Flight flight) {

        FlightEntity entity = FlightMapper.toEntity(flight,
            airlineJpaRepository.getReferenceById(flight.getAirline().getId()),
            airportJpaRepository.getReferenceById(flight.getDepartureAirport().getId()),
            airportJpaRepository.getReferenceById(flight.getArrivalAirport().getId())
        );
        FlightEntity saved = flightJpaRepository.save(entity);
        return FlightMapper.toDomain(saved);
    }

    @Override
    public boolean existsByFlightNumber(String flightNumber) {
        return flightJpaRepository.existsByFlightNumber(flightNumber);
    }
}
