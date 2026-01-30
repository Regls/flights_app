package springboot.aviation.infrastructure.mapper;

import springboot.aviation.domain.airline.Airline;
import springboot.aviation.infrastructure.persistence.airline.AirlineEntity;


public class AirlineMapper {
    
    public static Airline toDomain(AirlineEntity entity) {
        return Airline.restore(
            entity.getId(),
            entity.getIataCode(),
            entity.getAirlineName(),
            entity.getStatus()
        );
    }

    public static AirlineEntity toEntity(Airline airline) {
        AirlineEntity entity = new AirlineEntity();
        entity.setId(airline.getId());
        entity.setIataCode(airline.getIataCode());
        entity.setAirlineName(airline.getAirlineName());
        entity.setStatus(airline.getStatus());
        return entity;
    }
}
