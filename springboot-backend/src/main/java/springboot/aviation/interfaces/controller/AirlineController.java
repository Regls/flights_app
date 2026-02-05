package springboot.aviation.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import springboot.aviation.application.airline.service.AirlineQueryService;
import springboot.aviation.application.airline.usecase.*;
import springboot.aviation.application.flight.service.FlightQueryService;
import springboot.aviation.domain.airline.Airline;
import springboot.aviation.interfaces.dto.request.airline.ChangeAirlineRequest;
import springboot.aviation.interfaces.dto.request.airline.CreateAirlineRequest;
import springboot.aviation.interfaces.dto.response.airline.AirlineResponse;
import springboot.aviation.interfaces.dto.response.flight.FlightResponse;

@Tag(name="Airlines", description = "Airline management endpoints")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/airlines")
public class AirlineController {
    
    private final AirlineQueryService airlineQueryService;
    private final FlightQueryService flightQueryService;
    private final CreateAirlineUseCase createAirlineUseCase;
    private final ActivateAirlineUseCase activateAirlineUseCase;
    private final SuspendAirlineUseCase suspendAirlineUseCase;
    private final ChangeAirlineNameUseCase changeAirlineNameUseCase;

    public AirlineController(
        AirlineQueryService airlineQueryService,
        FlightQueryService flightQueryService,
        CreateAirlineUseCase createAirlineUseCase,
        ActivateAirlineUseCase activateAirlineUseCase,
        SuspendAirlineUseCase suspendAirlineUseCase,
        ChangeAirlineNameUseCase changeAirlineNameUseCase
    ){
        this.airlineQueryService = airlineQueryService;
        this.flightQueryService = flightQueryService;
        this.createAirlineUseCase = createAirlineUseCase;
        this.activateAirlineUseCase = activateAirlineUseCase;
        this.suspendAirlineUseCase = suspendAirlineUseCase;
        this.changeAirlineNameUseCase = changeAirlineNameUseCase;
    }

    @Operation(summary = "Get all airlines")
    @GetMapping
    public List<AirlineResponse> findAll() {
        return airlineQueryService.findAll().stream()
                .map(AirlineResponse::fromDomain)
                .toList();
    }

    @Operation(summary = "Get airline by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Airline found"),
        @ApiResponse(responseCode = "404", description = "Airline not found")
    })
    @GetMapping("/{id}")
    public AirlineResponse findById(@PathVariable Long id) {
        Airline airline = airlineQueryService.findById(id);
        return AirlineResponse.fromDomain(airline);
    }

    @Operation(summary = "Get airline by iata code")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Airline found"),
        @ApiResponse(responseCode = "404", description = "Airline not found")
    })
    @GetMapping("/iata/{iataCode}")
    public AirlineResponse findByIataCode(@PathVariable String iataCode) {
        Airline airline = airlineQueryService.findByIataCode(iataCode);
        return AirlineResponse.fromDomain(airline);
    }

    @Operation(summary = "Get all flights from a airline")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flights found"),
        @ApiResponse(responseCode = "404", description = "Airline not found")
    })
    @GetMapping("/{id}/flights")
    public List<FlightResponse> findFlights(@PathVariable Long id) {
        return flightQueryService.findAllFlightsByAirline(id).stream()
                .map(FlightResponse::fromDomain)
                .toList();
    }

    @Operation(summary = "Create a new airline")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Airline created" ),
        @ApiResponse(responseCode = "400", description = "Invalid Data" )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AirlineResponse create(@RequestBody CreateAirlineRequest request) {
        Airline airline = createAirlineUseCase.execute(
            request.iataCode(),
            request.airlineName()
        );
        return AirlineResponse.fromDomain(airline);
    }

    @Operation(summary = "Change airline name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Airline name changed"),
        @ApiResponse(responseCode = "400", description = "Invalid Data"),
        @ApiResponse(responseCode = "404", description = "Airline not found")
    })
    @PutMapping("/{id}/name")
    public AirlineResponse changeName(@PathVariable Long id, @RequestBody ChangeAirlineRequest request) {
        Airline airline = changeAirlineNameUseCase.execute(
                id,
                request.airlineName()
        );
        return AirlineResponse.fromDomain(airline);
    }

    @Operation(summary = "Activate airline")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Airline activated"),
        @ApiResponse(responseCode = "404", description = "Airline not found"),
        @ApiResponse(responseCode = "400", description = "Airline already active")
    })
    @PutMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Long id) {
        activateAirlineUseCase.execute(id);
    }

    @Operation(summary = "Suspend airline")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Airline suspended"),
        @ApiResponse(responseCode = "404", description = "Airline not found"),
        @ApiResponse(responseCode = "400", description = "Airline already suspended")
    })
    @PutMapping("/{id}/suspend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void suspend(@PathVariable Long id) {
        suspendAirlineUseCase.execute(id);
    }
}
