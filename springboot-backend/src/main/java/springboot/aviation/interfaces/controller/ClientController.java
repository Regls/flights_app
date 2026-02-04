package springboot.aviation.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import springboot.aviation.application.client.service.ClientQueryService;
import springboot.aviation.application.client.usecase.*;
import springboot.aviation.domain.client.Client;
import springboot.aviation.interfaces.dto.request.client.ChangeClientRequest;
import springboot.aviation.interfaces.dto.request.client.CreateClientRequest;
import springboot.aviation.interfaces.dto.response.client.ClientResponse;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    
    private final ClientQueryService clientQueryService;
    private final CreateClientUseCase createClientUseCase;
    private final ActivateClientUseCase activateClientUseCase;
    private final DeactivateClientUseCase deactivateClientUseCase;
    private final ChangeClientNameUseCase changeClientNameUseCase;

    public ClientController(
            ClientQueryService clientQueryService,
            CreateClientUseCase createClientUseCase,
            ActivateClientUseCase activateClientUseCase,
            DeactivateClientUseCase deactivateClientUseCase,
            ChangeClientNameUseCase changeClientNameUseCase
    ){
        this.clientQueryService = clientQueryService;
        this.createClientUseCase = createClientUseCase;
        this.activateClientUseCase = activateClientUseCase;
        this.deactivateClientUseCase = deactivateClientUseCase;
        this.changeClientNameUseCase = changeClientNameUseCase;
    }

    @GetMapping
    public List<ClientResponse> findAll() {
        return clientQueryService.findAll().stream()
                .map(ClientResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{id}")
    public ClientResponse findById(@PathVariable Long id) {
        Client client = clientQueryService.findById(id);
        return ClientResponse.fromDomain(client);
    }
    
    @GetMapping("/cpf/{cpf}")
    public ClientResponse findByCpf(@PathVariable String cpf) {
        Client client = clientQueryService.findByCpf(cpf);
        return ClientResponse.fromDomain(client);
    }

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

    @PutMapping("/{id}/name")
    public ClientResponse changeName(@PathVariable Long id, @RequestBody ChangeClientRequest request) {
        Client client = changeClientNameUseCase.execute(
                id,
                request.clientFirstName(),
                request.clientLastName()
        );
        return ClientResponse.fromDomain(client);
    }

    @PutMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Long id) {
        activateClientUseCase.execute(id);
    }

    @PutMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        deactivateClientUseCase.execute(id);
    }
}
