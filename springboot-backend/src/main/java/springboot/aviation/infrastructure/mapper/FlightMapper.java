package springboot.aviation.infrastructure.mapper;

import springboot.aviation.domain.flight.Flight;
import springboot.aviation.infrastructure.persistence.airline.AirlineEntity;
import springboot.aviation.infrastructure.persistence.airport.AirportEntity;
import springboot.aviation.infrastructure.persistence.flight.FlightEntity;


public class FlightMapper {
    
    public static Flight toDomain(FlightEntity entity) {
        return Flight.restore(
            entity.getId(),
            entity.getFlightNumber(),
            AirlineMapper.toDomain(entity.getAirline()),
            AirportMapper.toDomain(entity.getDepartureAirport()),
            AirportMapper.toDomain(entity.getArrivalAirport()),
            entity.getDepartureTime(),
            entity.getArrivalTime(),
            entity.getStatus()
        );
    }

    public static FlightEntity toEntity(Flight flight, AirlineEntity airlineEntity,
                    AirportEntity departureAirportEntity, AirportEntity arrivalAirportEntity) {
        FlightEntity entity = new FlightEntity();
        entity.setId(flight.getId());
        entity.setFlightNumber(flight.getFlightNumber());
        entity.setAirline(airlineEntity);
        entity.setDepartureAirport(departureAirportEntity);
        entity.setArrivalAirport(arrivalAirportEntity);
        entity.setDepartureTime(flight.getDepartureTime());
        entity.setArrivalTime(flight.getArrivalTime());
        entity.setStatus(flight.getStatus());
        return entity;
    }
}
