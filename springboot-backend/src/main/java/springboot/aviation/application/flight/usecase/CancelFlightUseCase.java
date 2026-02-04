package springboot.aviation.application.flight.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import springboot.aviation.domain.flight.Flight;
import springboot.aviation.domain.booking.Booking;
import springboot.aviation.domain.flight.FlightRepository;
import springboot.aviation.domain.booking.BookingRepository;
import springboot.aviation.exception.BusinessException;


@Service
public class CancelFlightUseCase {
    
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public CancelFlightUseCase(FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Flight execute(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        if(!flight.isScheduled()) throw new BusinessException("Only scheduled flights can be cancelled");

        List<Booking> activeBookings = bookingRepository.findActiveByFlight(id);

        for (Booking booking : activeBookings) {
            booking.cancelDueFlightCancel();
            bookingRepository.save(booking);
        }

        flight.cancel();
        return flightRepository.save(flight);
    }
}
