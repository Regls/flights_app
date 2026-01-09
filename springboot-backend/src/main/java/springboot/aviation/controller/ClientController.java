package springboot.aviation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot.aviation.dto.request.ChangeClientRequest;
import springboot.aviation.dto.request.CreateClientRequest;
import springboot.aviation.dto.response.ClientResponse;
import springboot.aviation.dto.response.MessageResponse;
import springboot.aviation.model.Client;
import springboot.aviation.service.ClientService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> list() {
        List<ClientResponse> response = clientService.findAll()
            .stream()
            .map(ClientResponse::from)
            .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> clientById(@PathVariable Long id) {
        Client client = clientService.findById(id);

        return ResponseEntity.ok(ClientResponse.from(client));
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody CreateClientRequest request) {
        Client client = clientService.createClient(request);

        return ResponseEntity.ok(ClientResponse.from(client));
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<ClientResponse> changeClientName(@PathVariable Long id, @RequestBody ChangeClientRequest request) {
        Client client= clientService.changeClientName(id, request);

        return ResponseEntity.ok(ClientResponse.from(client));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<MessageResponse> activate(@PathVariable Long id) {
        clientService.activate(id);
        return ResponseEntity.ok(MessageResponse.of("Client with id " + id + " was successfully activated"));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<MessageResponse> deactivate(@PathVariable Long id) {
        clientService.deactivate(id);
        return ResponseEntity.ok(MessageResponse.of("Client with id " + id + " was successfully deactivated"));
    }
    
}
