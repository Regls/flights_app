package springboot.aviation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.aviation.model.Booking;
import springboot.aviation.model.BookingStatus;
import springboot.aviation.model.Client;
import springboot.aviation.model.Flight;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByClientAndFlightAndStatusIn(Client client, Flight flight, List<BookingStatus> statuses);
    
    List<Booking> findByFlight(Flight flight);

    List<Booking> findByClient(Client client);

    List<Booking> findByClientAndStatusIn(Client client, List<BookingStatus> statuses);

    List<Booking> findByFlightAndStatusIn(Flight flight, List<BookingStatus> statuses);

    
}
