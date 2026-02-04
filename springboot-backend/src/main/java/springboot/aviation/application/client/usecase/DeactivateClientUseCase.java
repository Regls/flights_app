package springboot.aviation.application.client.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.booking.Booking;
import springboot.aviation.domain.booking.BookingRepository;
import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.client.ClientRepository;
import springboot.aviation.exception.BusinessException;


@Service
public class DeactivateClientUseCase {
    
    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;

    public DeactivateClientUseCase(ClientRepository clientRepository, BookingRepository bookingRepository) {
        this.clientRepository = clientRepository;
        this.bookingRepository = bookingRepository;
    }

    public void execute(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow();
        
        if (!client.isActive()) throw new BusinessException("Client already deactivated");

        List<Booking> activeBookings = bookingRepository.findActiveByClient(id);

        for (Booking booking : activeBookings) {
            booking.cancelDueClientDeactivate();
            bookingRepository.save(booking);
        }
        
        client.deactivate();
        clientRepository.save(client);
    }
}
