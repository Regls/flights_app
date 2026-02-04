package springboot.aviation.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import springboot.aviation.application.airport.service.AirportQueryService;
import springboot.aviation.application.airport.usecase.*;
import springboot.aviation.domain.airport.Airport;
import springboot.aviation.interfaces.dto.request.airport.ChangeAirportRequest;
import springboot.aviation.interfaces.dto.request.airport.CreateAirportRequest;
import springboot.aviation.interfaces.dto.response.airport.AirportResponse;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/airports")
public class AirportController {
    
    private final AirportQueryService airportQueryService;
    private final CreateAirportUseCase createAirportUseCase;
    private final OpenAirportUseCase openAirportUseCase;
    private final CloseAirportUseCase closeAirportUseCase;
    private final ChangeAirportNameUseCase changeClientNameUseCase;

    public AirportController(
        AirportQueryService airportQueryService, 
        CreateAirportUseCase createAirportUseCase,
        OpenAirportUseCase openAirportUseCase,
        CloseAirportUseCase closeAirportUseCase,
        ChangeAirportNameUseCase changeClientNameUseCase
    ){
        this.airportQueryService = airportQueryService;
        this.createAirportUseCase = createAirportUseCase;
        this.openAirportUseCase = openAirportUseCase;
        this.closeAirportUseCase = closeAirportUseCase;
        this.changeClientNameUseCase = changeClientNameUseCase;
    }

    @GetMapping
    public List<AirportResponse> findAll() {
        return airportQueryService.findAll().stream()
                .map(AirportResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{id}")
    public AirportResponse findById(@PathVariable Long id) {
        Airport airport = airportQueryService.findById(id);
        return AirportResponse.fromDomain(airport);
    }

    @GetMapping("/iata/{iataCode}")
    public AirportResponse findByIataCode(@PathVariable String iataCode) {
        Airport airport = airportQueryService.findByIataCode(iataCode);
        return AirportResponse.fromDomain(airport);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AirportResponse create(@RequestBody CreateAirportRequest request) {
        Airport airport = createAirportUseCase.execute(
            request.iataCode(),
            request.airportName(),
            request.city()
        );
        return AirportResponse.fromDomain(airport);
    }

    @PutMapping("/{id}/name")
    public AirportResponse changeName(@PathVariable Long id, @RequestBody ChangeAirportRequest request) {
        Airport airport = changeClientNameUseCase.execute(
                id,
                request.airportName()
        );
        return AirportResponse.fromDomain(airport);
    }

    @PutMapping("/{id}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void open(@PathVariable Long id) {
        openAirportUseCase.execute(id);
    }

    @PutMapping("/{id}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void close(@PathVariable Long id) {
        closeAirportUseCase.execute(id);
    }
}
