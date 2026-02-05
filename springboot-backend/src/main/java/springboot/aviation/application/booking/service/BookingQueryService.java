package springboot.aviation.application.booking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.aviation.domain.booking.Booking;
import springboot.aviation.domain.booking.BookingRepository;
import springboot.aviation.exception.ResourceNotFoundException;


@Service
public class BookingQueryService {
    
    private final BookingRepository bookingRepository;

    public BookingQueryService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
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
        return bookingRepository.findActiveByClient(clientId);
    }
}
