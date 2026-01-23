/*package springboot.aviation.controller;

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

import springboot.aviation.dto.request.CreateFlightRequest;
import springboot.aviation.dto.response.BookingResponse;
import springboot.aviation.dto.response.FlightResponse;
import springboot.aviation.dto.response.MessageResponse;
import springboot.aviation.model.Flight;
import springboot.aviation.service.BookingService;
import springboot.aviation.service.FlightService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {
    
    private final FlightService flightService;

    private final BookingService bookingService;

    public FlightController(FlightService flightService, BookingService bookingService) {
        this.flightService = flightService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> list() {
        List<FlightResponse> response = flightService.findAll()
            .stream()
            .map(FlightResponse::from)
            .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> flightById(@PathVariable Long id) {
        Flight flight = flightService.findById(id);

        return ResponseEntity.ok(FlightResponse.from(flight));
    }

    @GetMapping("/{id}/bookings")
    public List<BookingResponse> bookingsByFlight(@PathVariable Long id) {
        return bookingService.findByFlightId(id);
    }

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@RequestBody CreateFlightRequest request) {
        Flight flight = flightService.createFlight(request);

        return ResponseEntity.ok(FlightResponse.from(flight));
    }

    @PutMapping("/{id}/depart")
    public ResponseEntity<MessageResponse> depart(@PathVariable Long id) {
        flightService.depart(id);
        return ResponseEntity.ok(MessageResponse.of("Flight with id " + id + " was successfully departed"));
    }

    @PutMapping("/{id}/arrive")
    public ResponseEntity<MessageResponse> arrive(@PathVariable Long id) {
        flightService.arrive(id);
        return ResponseEntity.ok(MessageResponse.of("Flight with id " + id + " was successfully arrived"));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<MessageResponse> cancel(@PathVariable Long id) {
        flightService.cancel(id);
        return ResponseEntity.ok(MessageResponse.of("Flight with id " + id + " was successfully cancelled"));
    }
    
}
*/