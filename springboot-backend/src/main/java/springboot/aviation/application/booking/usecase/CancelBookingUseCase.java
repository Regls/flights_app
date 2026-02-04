package springboot.aviation.application.booking.usecase;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.booking.Booking;
import springboot.aviation.domain.booking.BookingRepository;


@Service
public class CancelBookingUseCase {
    
    private final BookingRepository bookingRepository;

    public CancelBookingUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking execute(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow();
        
        booking.cancel();
        return bookingRepository.save(booking);
    }
}
