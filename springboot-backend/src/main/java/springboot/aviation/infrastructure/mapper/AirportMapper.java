package springboot.aviation.infrastructure.mapper;

import springboot.aviation.domain.airport.Airport;
import springboot.aviation.infrastructure.persistence.airport.AirportEntity;


public class AirportMapper {
    
    public static Airport toDomain(AirportEntity entity) {
        return Airport.restore(
            entity.getId(),
            entity.getIataCode(),
            entity.getAirportName(),
            entity.getCity(),
            entity.getStatus()
        );
    }
    
    public static AirportEntity toEntity(Airport airport) {
        AirportEntity entity = new AirportEntity();
        entity.setId(airport.getId());
        entity.setIataCode(airport.getIataCode());
        entity.setAirportName(airport.getAirportName());
        entity.setCity(airport.getCity());
        entity.setStatus(airport.getStatus());
        return entity;
    }
}
