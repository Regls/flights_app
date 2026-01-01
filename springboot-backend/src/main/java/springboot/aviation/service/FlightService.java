package springboot.aviation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.aviation.dto.CreateFlightRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Airline;
import springboot.aviation.model.Airport;
import springboot.aviation.model.Booking;
import springboot.aviation.model.BookingStatus;
import springboot.aviation.model.Flight;
import springboot.aviation.repository.AirlineRepository;
import springboot.aviation.repository.AirportRepository;
import springboot.aviation.repository.FlightRepository;
import springboot.aviation.repository.BookingRepository;


@Service
public class FlightService {
    
    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final BookingRepository bookingRepository;

    public FlightService(FlightRepository flightRepository, AirlineRepository airlineRepository, AirportRepository airportRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.airlineRepository = airlineRepository;
        this.airportRepository = airportRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Flight findById(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
    }

    public Flight createFlight(CreateFlightRequest request) {

        if (flightRepository.existsByFlightNumber(request.flightNumber)) {
            throw new BusinessException("Flight with flight number already exists.");
        }

        Airline airline = airlineRepository.findById(request.airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));

        Airport departure = airportRepository.findById(request.departureAirportId)
                .orElseThrow(() -> new ResourceNotFoundException("Departure airport not found"));

        Airport arrival = airportRepository.findById(request.arrivalAirportId)
                .orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found"));
        
        Flight flight = Flight.createFlight(
                request.flightNumber,
                airline,
                departure,
                arrival,
                request.departureTime,
                request.arrivalTime
        );

        return flightRepository.save(flight);
    }

    public void depart(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        flight.depart();
        flightRepository.save(flight);
    }

    public void arrive(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        if (!flight.isInFlight()) {
            throw new BusinessException("Only in-flight flights can arrive.");
        }

        flight.arrive();
        flightRepository.save(flight);
    }

    @Transactional(rollbackFor = BusinessException.class)
    public void cancel(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        List<Booking> bookings = bookingRepository.findByFlightAndStatusIn(flight, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED));

        for (Booking booking : bookings) {
                booking.cancel();
        }

        flight.cancel();

        bookingRepository.saveAll(bookings);
        flightRepository.save(flight);
    }
}
