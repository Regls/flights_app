package springboot.aviation.application.booking.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.booking.Booking;
import springboot.aviation.domain.booking.BookingRepository;


@Service
public class ConfirmBookingUseCase {
    
    private final BookingRepository bookingRepository;

    public ConfirmBookingUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking execute(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.confirm();
        return bookingRepository.save(booking);
    }
}
