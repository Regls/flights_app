package springboot.aviation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.aviation.dto.request.CreateBookingRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Booking;
import springboot.aviation.model.BookingStatus;
import springboot.aviation.model.Client;
import springboot.aviation.model.Flight;
import springboot.aviation.repository.BookingRepository;
import springboot.aviation.repository.ClientRepository;
import springboot.aviation.repository.FlightRepository;


@Service
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final FlightRepository flightRepository;

    public BookingService(BookingRepository bookingRepository, ClientRepository clientRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.clientRepository = clientRepository;
        this.flightRepository = flightRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking findById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public Booking createBooking(CreateBookingRequest request) {

        Client client = clientRepository.findById(request.clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Flight flight = flightRepository.findById(request.flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        if (bookingRepository.existsByClientAndFlightAndStatusIn(client, flight, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED))) {
            throw new BusinessException("Client already has an active booking for this flight");
        }
        
        Booking booking = Booking.createBooking(client, flight);

        return bookingRepository.save(booking);

    }

    public void confirm(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if(!booking.isCreated()) throw new BusinessException("Only created bookings can be confirmed");
        
        booking.confirm();
        bookingRepository.save(booking);
    }

    public void cancel(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if(booking.isCancelled()) throw new BusinessException("Cancelled bookings cant be cancelled");

        booking.cancel();
        bookingRepository.save(booking);
    }
}
