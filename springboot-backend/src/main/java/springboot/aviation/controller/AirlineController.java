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
import springboot.aviation.dto.response.AirlineResponse;
import springboot.aviation.dto.response.MessageResponse;
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
    public ResponseEntity<List<AirlineResponse>> list() {
        List<AirlineResponse> response = airlineService.findAll()
            .stream()
            .map(AirlineResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirlineResponse> airlineById(@PathVariable Long id) {
        Airline airline = airlineService.findById(id);

        return ResponseEntity.ok(AirlineResponse.from(airline));
    }

    @PostMapping
    public ResponseEntity<AirlineResponse> createAirline(@RequestBody CreateAirlineRequest request) {
        Airline airline = airlineService.createAirline(request);

        return ResponseEntity.ok(AirlineResponse.from(airline));
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<AirlineResponse> changeAirlineName(@PathVariable Long id, @RequestBody ChangeAirlineRequest request) {
        Airline airline = airlineService.changeAirlineName(id, request);

        return ResponseEntity.ok(AirlineResponse.from(airline));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<MessageResponse> activate(@PathVariable Long id) {
        airlineService.activate(id);
        return ResponseEntity.ok(MessageResponse.of("Airline with id " + id + " was successfully activated"));
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<MessageResponse> suspend(@PathVariable Long id) {
        airlineService.suspend(id);
        return ResponseEntity.ok(MessageResponse.of("Airline with id " + id + " was successfully suspended"));
    }

}
