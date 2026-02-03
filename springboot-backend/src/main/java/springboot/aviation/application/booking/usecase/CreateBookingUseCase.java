package springboot.aviation.application.booking.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.application.booking.port.BookingCodeGenerator;
import springboot.aviation.domain.booking.Booking;
import springboot.aviation.domain.booking.BookingRepository;
import springboot.aviation.domain.booking.BookingStatus;
import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.flight.Flight;
import springboot.aviation.domain.client.ClientRepository;
import springboot.aviation.domain.flight.FlightRepository;
import springboot.aviation.exception.BusinessException;
import java.util.List;


@Service
public class CreateBookingUseCase {
    
    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final FlightRepository flightRepository;
    private final BookingCodeGenerator bookingCodeGenerator;

    public CreateBookingUseCase(BookingRepository bookingRepository, ClientRepository clientRepository, FlightRepository flightRepository, BookingCodeGenerator bookingCodeGenerator) {
        this.bookingRepository = bookingRepository;
        this.clientRepository = clientRepository;
        this.flightRepository = flightRepository;
        this.bookingCodeGenerator = bookingCodeGenerator;
    }

    public Booking execute(Long clientId, Long flightId) {

        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new BusinessException("Client not found"));
            
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new BusinessException("Flight not found"));

        validateNoActiveBooking(client, flight);
        
        String bookingCode;
        do {
            bookingCode = bookingCodeGenerator.generate();
        } while (bookingRepository.existsByBookingCode(bookingCode));
        
        Booking booking = Booking.create(client, flight, bookingCode);
        return bookingRepository.save(booking);
    }

    private void validateNoActiveBooking(Client client, Flight flight) {
        boolean exists = bookingRepository.existsByClientAndFlightAndStatusIn(client, flight, List.of(BookingStatus.CREATED, BookingStatus.CONFIRMED));
        if (exists) throw new BusinessException("Client already has an active booking for this flight.");
    }
}
