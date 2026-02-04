package springboot.aviation.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import springboot.aviation.application.booking.service.BookingQueryService;
import springboot.aviation.application.booking.usecase.*;
import springboot.aviation.domain.booking.Booking;
import springboot.aviation.interfaces.dto.request.booking.CreateBookingRequest;
import springboot.aviation.interfaces.dto.response.booking.BookingResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    
    private final BookingQueryService bookingQueryService;
    private final CreateBookingUseCase createBookingUseCase;
    private final ConfirmBookingUseCase confirmBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;

    public BookingController(
        BookingQueryService bookingQueryService,
        CreateBookingUseCase createBookingUseCase,
        ConfirmBookingUseCase confirmBookingUseCase,
        CancelBookingUseCase cancelBookingUseCase
    ) {
        this.bookingQueryService = bookingQueryService;
        this.createBookingUseCase = createBookingUseCase;
        this.confirmBookingUseCase = confirmBookingUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
    }

    @GetMapping
    public List<BookingResponse> findAll() {
        return bookingQueryService.findAll().stream()
                .map(BookingResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{id}")
    public BookingResponse findById(@PathVariable Long id) {
        Booking booking = bookingQueryService.findById(id);
        return BookingResponse.fromDomain(booking);
    }

    @GetMapping("/booking-code/{bookingCode}")
    public BookingResponse findByBookingCode(@PathVariable String bookingCode) {
        Booking booking = bookingQueryService.findByBookingCode(bookingCode);
        return BookingResponse.fromDomain(booking);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@RequestBody CreateBookingRequest request) {
        Booking booking = createBookingUseCase.execute(
            request.clientId(),
            request.flightId()
        );
        return BookingResponse.fromDomain(booking);
    }

    @PutMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@PathVariable Long id) {
        confirmBookingUseCase.execute(id);
    }

    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        cancelBookingUseCase.execute(id);
    }
}
