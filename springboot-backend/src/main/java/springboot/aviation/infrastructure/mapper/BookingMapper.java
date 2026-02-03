package springboot.aviation.infrastructure.mapper;

import springboot.aviation.domain.booking.Booking;
import springboot.aviation.infrastructure.persistence.client.ClientEntity;
import springboot.aviation.infrastructure.persistence.flight.FlightEntity;
import springboot.aviation.infrastructure.persistence.booking.BookingEntity;


public class BookingMapper {

    public static Booking toDomain(BookingEntity entity) {
        return Booking.restore(
            entity.getId(),
            ClientMapper.toDomain(entity.getclient()),
            FlightMapper.toDomain(entity.getFlight()),
            entity.getBookingCode(),
            entity.getCreatedAt(),
            entity.getStatus()
        );
    }  
    
    public static BookingEntity toEntity(Booking booking, ClientEntity clientEntity,
                    FlightEntity flightEntity) {
        BookingEntity entity = new BookingEntity();
        entity.setId(booking.getId());
        entity.setClient(clientEntity);
        entity.setFlight(flightEntity);
        entity.setBookingCode(booking.getBookingCode());
        entity.setCreatedAt(booking.getCreatedAt());
        entity.setStatus(booking.getStatus());
        return entity;
    } 
}
