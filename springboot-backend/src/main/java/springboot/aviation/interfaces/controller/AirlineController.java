package springboot.aviation.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import springboot.aviation.application.airline.service.AirlineQueryService;
import springboot.aviation.application.airline.usecase.*;
import springboot.aviation.domain.airline.Airline;
import springboot.aviation.dto.request.airline.ChangeAirlineRequest;
import springboot.aviation.dto.request.airline.CreateAirlineRequest;
import springboot.aviation.dto.response.airline.AirlineResponse;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/airlines")
public class AirlineController {
    
    private final AirlineQueryService airlineQueryService;
    private final CreateAirlineUseCase createAirlineUseCase;
    private final ActivateAirlineUseCase activateAirlineUseCase;
    private final SuspendAirlineUseCase suspendAirlineUseCase;
    private final ChangeAirlineNameUseCase changeAirlineNameUseCase;

    public AirlineController(
        AirlineQueryService airlineQueryService,
        CreateAirlineUseCase createAirlineUseCase,
        ActivateAirlineUseCase activateAirlineUseCase,
        SuspendAirlineUseCase suspendAirlineUseCase,
        ChangeAirlineNameUseCase changeAirlineNameUseCase
    ){
        this.airlineQueryService = airlineQueryService;
        this.createAirlineUseCase = createAirlineUseCase;
        this.activateAirlineUseCase = activateAirlineUseCase;
        this.suspendAirlineUseCase = suspendAirlineUseCase;
        this.changeAirlineNameUseCase = changeAirlineNameUseCase;
    }

    @GetMapping
    public List<AirlineResponse> findAll() {
        return airlineQueryService.findAll().stream()
                .map(AirlineResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{id}")
    public AirlineResponse findById(@PathVariable Long id) {
        Airline airline = airlineQueryService.findById(id);
        return AirlineResponse.fromDomain(airline);
    }

    @GetMapping("/iata/{iataCode}")
    public AirlineResponse findByIataCode(@PathVariable String iataCode) {
        Airline airline = airlineQueryService.findByIataCode(iataCode);
        return AirlineResponse.fromDomain(airline);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AirlineResponse create(@RequestBody CreateAirlineRequest request) {
        Airline airline = createAirlineUseCase.execute(
            request.iataCode(),
            request.airlineName()
        );
        return AirlineResponse.fromDomain(airline);
    }

    @PutMapping("/{id}/name")
    public AirlineResponse changeName(@PathVariable Long id, @RequestBody ChangeAirlineRequest request) {
        Airline airline = changeAirlineNameUseCase.execute(
                id,
                request.airlineName()
        );
        return AirlineResponse.fromDomain(airline);
    }

    @PutMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Long id) {
        activateAirlineUseCase.execute(id);
    }

    @PutMapping("/{id}/suspend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void suspend(@PathVariable Long id) {
        suspendAirlineUseCase.execute(id);
    }
}
