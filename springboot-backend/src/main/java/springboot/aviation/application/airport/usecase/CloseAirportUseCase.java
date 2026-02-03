package springboot.aviation.application.airport.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import springboot.aviation.domain.airport.Airport;
import springboot.aviation.domain.flight.Flight;
import springboot.aviation.domain.booking.Booking;
import springboot.aviation.domain.airport.AirportRepository;
import springboot.aviation.domain.booking.BookingRepository;
import springboot.aviation.domain.flight.FlightRepository;
import springboot.aviation.exception.BusinessException;


@Service
public class CloseAirportUseCase {
    
    private final AirportRepository airportRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public CloseAirportUseCase(AirportRepository airportRepository, FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void execute(Long airportId) {
        Airport airport = airportRepository.findById(airportId)
            .orElseThrow();
        
        if(!airport.isOpen()) throw new BusinessException("Airport is already closed");

        List<Flight> scheduledFlights = flightRepository.findScheduledFlightsByAirports(airportId);
        
        for (Flight flight : scheduledFlights) {
            flight.cancel();
            flightRepository.save(flight);

            List<Booking> activeBookings = bookingRepository.findActiveByFlight(flight.getId());

            for (Booking booking : activeBookings) {
                booking.cancelDueFlightCancel();
                bookingRepository.save(booking);
            }
        }

        airport.close();
        airportRepository.save(airport);
    }
}
