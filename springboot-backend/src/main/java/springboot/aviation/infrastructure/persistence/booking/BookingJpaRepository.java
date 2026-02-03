package springboot.aviation.infrastructure.persistence.booking;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import springboot.aviation.domain.booking.BookingStatus;
import springboot.aviation.infrastructure.persistence.client.ClientEntity;
import springboot.aviation.infrastructure.persistence.flight.FlightEntity;


public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long>{
    boolean existsByBookingCode(String bookingCode);
    boolean existsByClientAndFlightAndStatusIn(ClientEntity client, FlightEntity flight, List<BookingStatus> statuses);
    Optional<BookingEntity> findByBookingCode(String bookingCode);
}
