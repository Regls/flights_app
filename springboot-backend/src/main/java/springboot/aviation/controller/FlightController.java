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

import springboot.aviation.dto.CreateFlightRequest;
import springboot.aviation.model.Flight;
import springboot.aviation.service.FlightService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {
    
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> list() {
        return ResponseEntity.ok(flightService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> flightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody CreateFlightRequest request) {
        return ResponseEntity.ok(flightService.createFlight(request));
    }

    @PutMapping("/{id}/depart")
    public ResponseEntity<Flight> depart(@PathVariable Long id) {
        flightService.depart(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/arrive")
    public ResponseEntity<Flight> arrive(@PathVariable Long id) {
        flightService.arrive(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Flight> cancel(@PathVariable Long id) {
        flightService.cancel(id);
        return ResponseEntity.noContent().build();
    }
    
}
