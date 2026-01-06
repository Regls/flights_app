package springboot.aviation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot.aviation.dto.CreateAirportRequest;
import springboot.aviation.dto.ChangeAirportRequest;
import springboot.aviation.model.Airport;
import springboot.aviation.service.AirportService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public ResponseEntity<List<Airport>> list() {
        return ResponseEntity.ok(airportService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airport> airportById(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Airport> createAirport(@RequestBody CreateAirportRequest request) {
        return ResponseEntity.ok(airportService.createAirport(request));
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<Airport> changeAirportName(@PathVariable Long id, @RequestBody ChangeAirportRequest request) {
        return ResponseEntity.ok(airportService.changeAirportName(id, request));
    }

    @PutMapping("/{id}/open")
    public ResponseEntity<Void> open(@PathVariable Long id) {
        airportService.open(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Void> close(@PathVariable Long id) {
        airportService.close(id);
        return ResponseEntity.noContent().build();
    }

}
