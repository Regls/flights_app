package springboot.aviation.application.booking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.booking.Booking;
import springboot.aviation.domain.booking.BookingRepository;
import springboot.aviation.domain.client.ClientRepository;
import springboot.aviation.domain.flight.FlightRepository;
import springboot.aviation.exception.ResourceNotFoundException;


@Service
public class BookingQueryService {
    
    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final FlightRepository flightRepository;

    public BookingQueryService(BookingRepository bookingRepository, ClientRepository clientRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.clientRepository = clientRepository;
        this.flightRepository = flightRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public Booking findByBookingCode(String bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public List<Booking> findActiveByClient(Long clientId) {
        if(!clientRepository.existsById(clientId)) throw new ResourceNotFoundException("Client not found");
        return bookingRepository.findActiveByClient(clientId);
    }

    public List<Booking> findActiveByFlight(Long flightId) {
        if(!flightRepository.existsById(flightId)) throw new ResourceNotFoundException("Flight not found");
        return bookingRepository.findActiveByFlight(flightId);
    }
}
