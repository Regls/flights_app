package springboot.aviation.domain.booking;

import java.util.List;
import java.util.Optional;

import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.flight.Flight;


public interface BookingRepository {
    
    List<Booking> findAll();
    
    Optional<Booking> findById(Long id);

    Optional<Booking> findByBookingCode(String bookingCode);

    Booking save(Booking booking);

    boolean existsByBookingCode(String bookingCode);

    boolean existsByClientAndFlightAndStatusIn(Client client, Flight flight, List<BookingStatus> statuses);
}
