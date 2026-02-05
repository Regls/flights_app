package springboot.aviation.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import springboot.aviation.application.airport.service.AirportQueryService;
import springboot.aviation.application.airport.usecase.*;
import springboot.aviation.domain.airport.Airport;
import springboot.aviation.interfaces.dto.request.airport.ChangeAirportRequest;
import springboot.aviation.interfaces.dto.request.airport.CreateAirportRequest;
import springboot.aviation.interfaces.dto.response.airport.AirportResponse;

@Tag(name = "Airports", description = "Airport management endpoints")
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

    @Operation(summary = "Get all airports")
    @GetMapping
    public List<AirportResponse> findAll() {
        return airportQueryService.findAll().stream()
                .map(AirportResponse::fromDomain)
                .toList();
    }

    @Operation(summary = "Get airport by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Airport found"),
        @ApiResponse(responseCode = "404", description = "Airport not found")
    })
    @GetMapping("/{id}")
    public AirportResponse findById(@PathVariable Long id) {
        Airport airport = airportQueryService.findById(id);
        return AirportResponse.fromDomain(airport);
    }

    @Operation(summary = "Get airport by iata code")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Airport found"),
        @ApiResponse(responseCode = "404", description = "Airport not found")
    })
    @GetMapping("/iata/{iataCode}")
    public AirportResponse findByIataCode(@PathVariable String iataCode) {
        Airport airport = airportQueryService.findByIataCode(iataCode);
        return AirportResponse.fromDomain(airport);
    }

    @Operation(summary = "Create a new airport")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Airport created" ),
        @ApiResponse(responseCode = "400", description = "Invalid Data" )
    })
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

    @Operation(summary = "Change airport name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Airport name changed"),
        @ApiResponse(responseCode = "400", description = "Invalid Data"),
        @ApiResponse(responseCode = "404", description = "Airport not found")
    })
    @PutMapping("/{id}/name")
    public AirportResponse changeName(@PathVariable Long id, @RequestBody ChangeAirportRequest request) {
        Airport airport = changeClientNameUseCase.execute(
                id,
                request.airportName()
        );
        return AirportResponse.fromDomain(airport);
    }

    @Operation(summary = "Open airport")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Airport opened"),
        @ApiResponse(responseCode = "404", description = "Airport not found"),
        @ApiResponse(responseCode = "400", description = "Airport already open")
    })
    @PutMapping("/{id}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void open(@PathVariable Long id) {
        openAirportUseCase.execute(id);
    }

    @Operation(summary = "Close airport")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Airport closed"),
        @ApiResponse(responseCode = "404", description = "Airport not found"),
        @ApiResponse(responseCode = "400", description = "Airport already closed")
    })
    @PutMapping("/{id}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void close(@PathVariable Long id) {
        closeAirportUseCase.execute(id);
    }
}
