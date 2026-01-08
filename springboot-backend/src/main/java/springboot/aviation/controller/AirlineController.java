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

import springboot.aviation.dto.request.ChangeAirlineRequest;
import springboot.aviation.dto.request.CreateAirlineRequest;
import springboot.aviation.model.Airline;
import springboot.aviation.service.AirlineService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/airlines")
public class AirlineController {
    
    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @GetMapping
    public ResponseEntity<List<Airline>> list() {
        return ResponseEntity.ok(airlineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airline> airlineById(@PathVariable Long id) {
        return ResponseEntity.ok(airlineService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Airline> createAirline(@RequestBody CreateAirlineRequest request) {
        return ResponseEntity.ok(airlineService.createAirline(request));
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<Airline> changeAirlineName(@PathVariable Long id, @RequestBody ChangeAirlineRequest request) {
        return ResponseEntity.ok(airlineService.changeAirlineName(id, request));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        airlineService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<Void> suspend(@PathVariable Long id) {
        airlineService.suspend(id);
        return ResponseEntity.noContent().build();
    }

}
