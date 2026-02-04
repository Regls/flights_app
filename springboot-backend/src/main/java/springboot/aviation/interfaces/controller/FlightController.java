package springboot.aviation.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import springboot.aviation.application.flight.service.FlightQueryService;
import springboot.aviation.application.flight.usecase.*;
import springboot.aviation.domain.flight.Flight;
import springboot.aviation.interfaces.dto.request.flight.CreateFlightRequest;
import springboot.aviation.interfaces.dto.response.flight.FlightResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {
    
    private final FlightQueryService flightQueryService;
    private final CreateFlightUseCase createFlightUseCase;
    private final CancelFlightUseCase cancelFlightUseCase;
    private final DepartFlightUseCase departFlightUseCase;
    private final ArriveFlightUseCase arriveFlightUseCase;

    public FlightController(
        FlightQueryService flightQueryService,
        CreateFlightUseCase createFlightUseCase,
        CancelFlightUseCase cancelFlightUseCase,
        DepartFlightUseCase departFlightUseCase,
        ArriveFlightUseCase arriveFlightUseCase
    ) {
        this.flightQueryService = flightQueryService;
        this.createFlightUseCase = createFlightUseCase;
        this.cancelFlightUseCase = cancelFlightUseCase;
        this.departFlightUseCase = departFlightUseCase;
        this.arriveFlightUseCase = arriveFlightUseCase;
    }

    @GetMapping
    public List<FlightResponse> findAll() {
        return flightQueryService.findAll().stream()
                .map(FlightResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{id}")
    public FlightResponse findById(@PathVariable Long id) {
        Flight flight = flightQueryService.findById(id);
        return FlightResponse.fromDomain(flight);
    }

    @GetMapping("/flight-number/{flightNumber}")
    public FlightResponse findByFlightNumber(@PathVariable String flightNumber) {
        Flight flight = flightQueryService.findByFlightNumber(flightNumber);
        return FlightResponse.fromDomain(flight);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse create(@RequestBody CreateFlightRequest request) {
        Flight flight = createFlightUseCase.execute(
            request.flightNumber(),
            request.airlineId(),
            request.departureAirportId(),
            request.arrivalAirportId(),
            request.departureTime(),
            request.arrivalTime()
        );
        return FlightResponse.fromDomain(flight);
    }

    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        cancelFlightUseCase.execute(id);
    }

    @PutMapping("/{id}/depart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void depart(@PathVariable Long id) {
        departFlightUseCase.execute(id);
    }

    @PutMapping("/{id}/arrive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void arrive(@PathVariable Long id) {
        arriveFlightUseCase.execute(id);
    }
}
