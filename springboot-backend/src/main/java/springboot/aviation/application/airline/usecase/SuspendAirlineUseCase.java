package springboot.aviation.application.airline.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import springboot.aviation.domain.airline.Airline;
import springboot.aviation.domain.flight.Flight;
import springboot.aviation.domain.booking.Booking;
import springboot.aviation.domain.airline.AirlineRepository;
import springboot.aviation.domain.booking.BookingRepository;
import springboot.aviation.domain.flight.FlightRepository;
import springboot.aviation.exception.BusinessException;


@Service
public class SuspendAirlineUseCase {
    
    private final AirlineRepository airlineRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    
    public SuspendAirlineUseCase(AirlineRepository airlineRepository, FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.airlineRepository = airlineRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Airline execute(Long id) {
        Airline airline = airlineRepository.findById(id)
            .orElseThrow();

        if(!airline.isActive()) throw new BusinessException("Airline is already suspended");

        List<Flight> scheduledFlights = flightRepository.findScheduledFlightsByAirline(id);
        
        for (Flight flight : scheduledFlights) {
            flight.cancel();
            flightRepository.save(flight);

            List<Booking> activeBookings = bookingRepository.findActiveByFlight(flight.getId());

            for (Booking booking : activeBookings) {
                booking.cancelDueFlightCancel();
                bookingRepository.save(booking);
            }
        }

        airline.suspend();
        return airlineRepository.save(airline);
    }
}
