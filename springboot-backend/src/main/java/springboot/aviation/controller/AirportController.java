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

import springboot.aviation.dto.request.ChangeAirportRequest;
import springboot.aviation.dto.request.CreateAirportRequest;
import springboot.aviation.dto.response.AirportResponse;
import springboot.aviation.dto.response.FlightResponse;
import springboot.aviation.dto.response.MessageResponse;
import springboot.aviation.model.Airport;
import springboot.aviation.service.AirportService;
import springboot.aviation.service.FlightService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/airports")
public class AirportController {

    private final AirportService airportService;

    private final FlightService flightService;

    public AirportController(AirportService airportService, FlightService flightService) {
        this.airportService = airportService;
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<AirportResponse>> list() {
        List<AirportResponse> response = airportService.findAll()
            .stream()
            .map(AirportResponse::from)
            .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportResponse> airportById(@PathVariable Long id) {
        Airport airport = airportService.findById(id);

        return ResponseEntity.ok(AirportResponse.from(airport));
    }

    @GetMapping("/{id}/flights")
    public List<FlightResponse> flightsByAirport(@PathVariable Long id) {
        return flightService.findByAirportId(id);
    }

    @PostMapping
    public ResponseEntity<AirportResponse> createAirport(@RequestBody CreateAirportRequest request) {
        Airport airport = airportService.createAirport(request);

        return ResponseEntity.ok(AirportResponse.from(airport));
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<AirportResponse> changeAirportName(@PathVariable Long id, @RequestBody ChangeAirportRequest request) {
        Airport airport = airportService.changeAirportName(id, request);

        return ResponseEntity.ok(AirportResponse.from(airport));
    }

    @PutMapping("/{id}/open")
    public ResponseEntity<MessageResponse> open(@PathVariable Long id) {
        airportService.open(id);
        return ResponseEntity.ok(MessageResponse.of("Airport with id " + id + " was successfully opened"));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<MessageResponse> close(@PathVariable Long id) {
        airportService.close(id);
        return ResponseEntity.ok(MessageResponse.of("Airport with id " + id + " was successfully closed"));
    }

}
