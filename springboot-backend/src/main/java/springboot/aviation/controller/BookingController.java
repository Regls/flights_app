package springboot.aviation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot.aviation.dto.request.CreateBookingRequest;
import springboot.aviation.dto.response.BookingResponse;
import springboot.aviation.model.Booking;
import springboot.aviation.service.BookingService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> list() {
        List<BookingResponse> response = bookingService.findAll()
            .stream()
            .map(BookingResponse::from)
            .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> bookingById(@PathVariable Long id) {
        Booking booking = bookingService.findById(id);

        return ResponseEntity.ok(BookingResponse.from(booking));
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody CreateBookingRequest request) {
        Booking booking = bookingService.createBooking(request);

        return ResponseEntity.ok(BookingResponse.from(booking));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirm(@PathVariable Long id) {
        bookingService.confirm(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable Long id) {
        bookingService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
