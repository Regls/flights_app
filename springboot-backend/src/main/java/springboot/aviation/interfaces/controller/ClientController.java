package springboot.aviation.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import springboot.aviation.application.client.service.ClientQueryService;
import springboot.aviation.application.booking.service.BookingQueryService;
import springboot.aviation.application.client.usecase.*;
import springboot.aviation.domain.client.Client;
import springboot.aviation.interfaces.dto.request.client.ChangeClientRequest;
import springboot.aviation.interfaces.dto.request.client.CreateClientRequest;
import springboot.aviation.interfaces.dto.response.booking.BookingResponse;
import springboot.aviation.interfaces.dto.response.client.ClientResponse;

@Tag(name = "Clients", description = "Client management endpoints")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    
    private final ClientQueryService clientQueryService;
    private final BookingQueryService bookingQueryService;
    private final CreateClientUseCase createClientUseCase;
    private final ActivateClientUseCase activateClientUseCase;
    private final DeactivateClientUseCase deactivateClientUseCase;
    private final ChangeClientNameUseCase changeClientNameUseCase;

    public ClientController(
            ClientQueryService clientQueryService,
            BookingQueryService bookingQueryService,
            CreateClientUseCase createClientUseCase,
            ActivateClientUseCase activateClientUseCase,
            DeactivateClientUseCase deactivateClientUseCase,
            ChangeClientNameUseCase changeClientNameUseCase
    ){
        this.clientQueryService = clientQueryService;
        this.bookingQueryService = bookingQueryService;
        this.createClientUseCase = createClientUseCase;
        this.activateClientUseCase = activateClientUseCase;
        this.deactivateClientUseCase = deactivateClientUseCase;
        this.changeClientNameUseCase = changeClientNameUseCase;
    }

    @Operation(summary = "Get all clients")
    @GetMapping
    public List<ClientResponse> findAll() {
        return clientQueryService.findAll().stream()
                .map(ClientResponse::fromDomain)
                .toList();
    }

    @Operation(summary = "Get client by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client found"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public ClientResponse findById(@PathVariable Long id) {
        Client client = clientQueryService.findById(id);
        return ClientResponse.fromDomain(client);
    }
    
    @Operation(summary = "Get client by cpf")
    @GetMapping("/cpf/{cpf}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client found"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ClientResponse findByCpf(@PathVariable String cpf) {
        Client client = clientQueryService.findByCpf(cpf);
        return ClientResponse.fromDomain(client);
    }

    @Operation(summary = "Get all bookings from a client")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bookings found"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}/bookings")
    public List<BookingResponse> findBookings(@PathVariable Long id) {
        return bookingQueryService.findActiveByClient(id).stream()
                .map(BookingResponse::fromDomain)
                .toList();
    }

    @Operation(summary = "Create a new client")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Client created" ),
        @ApiResponse(responseCode = "400", description = "Invalid Data" )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse create(@RequestBody CreateClientRequest request) {
        Client client = createClientUseCase.execute(
            request.cpf(),
            request.clientFirstName(),
            request.clientLastName()
        );
        return ClientResponse.fromDomain(client);
    }

    @Operation(summary = "Change client name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client name changed"),
        @ApiResponse(responseCode = "400", description = "Invalid Data"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PutMapping("/{id}/name")
    public ClientResponse changeName(@PathVariable Long id, @RequestBody ChangeClientRequest request) {
        Client client = changeClientNameUseCase.execute(
                id,
                request.clientFirstName(),
                request.clientLastName()
        );
        return ClientResponse.fromDomain(client);
    }

    @Operation(summary = "Activate client")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Client activated"),
        @ApiResponse(responseCode = "404", description = "Client not found"),
        @ApiResponse(responseCode = "400", description = "Client already active")
    })
    @PutMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Long id) {
        activateClientUseCase.execute(id);
    }

    @Operation(summary = "Deactivate client")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Client activated"),
        @ApiResponse(responseCode = "404", description = "Client not found"),
        @ApiResponse(responseCode = "400", description = "Client already deactive")
    })
    @PutMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        deactivateClientUseCase.execute(id);
    }
}
