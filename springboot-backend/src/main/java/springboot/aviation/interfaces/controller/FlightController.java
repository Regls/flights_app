package springboot.aviation.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import springboot.aviation.application.flight.service.FlightQueryService;
import springboot.aviation.application.flight.usecase.*;
import springboot.aviation.domain.flight.Flight;
import springboot.aviation.interfaces.dto.request.flight.CreateFlightRequest;
import springboot.aviation.interfaces.dto.response.flight.FlightResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Flights", description = "Flight management endpoints")
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

    @Operation(summary = "Get all flights")
    @GetMapping
    public List<FlightResponse> findAll() {
        return flightQueryService.findAll().stream()
                .map(FlightResponse::fromDomain)
                .toList();
    }

    @Operation(summary = "Get flight by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flight found"),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    @GetMapping("/{id}")
    public FlightResponse findById(@PathVariable Long id) {
        Flight flight = flightQueryService.findById(id);
        return FlightResponse.fromDomain(flight);
    }

    @Operation(summary = "Get flight by flight number")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flight found"),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    @GetMapping("/flight-number/{flightNumber}")
    public FlightResponse findByFlightNumber(@PathVariable String flightNumber) {
        Flight flight = flightQueryService.findByFlightNumber(flightNumber);
        return FlightResponse.fromDomain(flight);
    }

    @Operation(summary = "Create a new flight")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Flight created" ),
        @ApiResponse(responseCode = "400", description = "Invalid Data" )
    })
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

    @Operation(summary = "Cancel flight")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Flight canceled"),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "400", description = "Flight already canceled or arrived")
    })
    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        cancelFlightUseCase.execute(id);
    }

    @Operation(summary = "Depart flight")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Flight departed"),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "400", description = "Flight not scheduled")
    })
    @PutMapping("/{id}/depart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void depart(@PathVariable Long id) {
        departFlightUseCase.execute(id);
    }

    @Operation(summary = "Arrive flight")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Flight arrived"),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "400", description = "Flight not in flight")
    })
    @PutMapping("/{id}/arrive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void arrive(@PathVariable Long id) {
        arriveFlightUseCase.execute(id);
    }
}
