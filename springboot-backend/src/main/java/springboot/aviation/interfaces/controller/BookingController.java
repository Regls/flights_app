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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Bookings", description = "Booking management endpoints")
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

    @Operation(summary = "Get all bookings")
    @GetMapping
    public List<BookingResponse> findAll() {
        return bookingQueryService.findAll().stream()
                .map(BookingResponse::fromDomain)
                .toList();
    }

    @Operation(summary = "Get booking by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Booking found"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    public BookingResponse findById(@PathVariable Long id) {
        Booking booking = bookingQueryService.findById(id);
        return BookingResponse.fromDomain(booking);
    }

    @Operation(summary = "Get booking by booking code")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Booking found"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/booking-code/{bookingCode}")
    public BookingResponse findByBookingCode(@PathVariable String bookingCode) {
        Booking booking = bookingQueryService.findByBookingCode(bookingCode);
        return BookingResponse.fromDomain(booking);
    }

    @Operation(summary = "Create a new booking")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Booking created" ),
        @ApiResponse(responseCode = "400", description = "Invalid Data" )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@RequestBody CreateBookingRequest request) {
        Booking booking = createBookingUseCase.execute(
            request.clientId(),
            request.flightId()
        );
        return BookingResponse.fromDomain(booking);
    }

    @Operation(summary = "Confirm booking")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Booking confirmed"),
        @ApiResponse(responseCode = "404", description = "Booking not found"),
        @ApiResponse(responseCode = "400", description = "Booking already confirmed or canceled")
    })
    @PutMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@PathVariable Long id) {
        confirmBookingUseCase.execute(id);
    }

    @Operation(summary = "Cancel booking")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Booking canceled"),
        @ApiResponse(responseCode = "404", description = "Booking not found"),
        @ApiResponse(responseCode = "400", description = "Booking already canceled")
    })
    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        cancelBookingUseCase.execute(id);
    }
}
