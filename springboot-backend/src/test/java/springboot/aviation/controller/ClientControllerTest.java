package springboot.aviation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import springboot.aviation.dto.request.ChangeClientRequest;
import springboot.aviation.dto.request.CreateClientRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Client;
import springboot.aviation.service.ClientService;


@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Client validClient() {
        return Client.createClient("12345678900", "New", "Client");
    }

    private CreateClientRequest validRequest() {
        CreateClientRequest request = new CreateClientRequest();
        request.cpf = "12345678900";
        request.clientFirstName = "New";
        request.clientLastName = "Client";
        return request;
    }

    private ChangeClientRequest validChangeRequest(){
        ChangeClientRequest request = new ChangeClientRequest();
        request.clientFirstName = "Newnew";
        request.clientLastName = "Clientclient";
        return request;
    }

    @Test
    void shouldReturnClientList() throws Exception {
        
        Client client = validClient();
        
        when(clientService.findAll()).thenReturn(List.of(client));

        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        
        verify(clientService).findAll();
    }

    @Test
    void shouldReturnClientById() throws Exception {

        Client client = validClient();

        when(clientService.findById(1L)).thenReturn(client);

        mockMvc.perform(get("/api/v1/clients/{id}", 1L))
                .andExpect(status().isOk());

        verify(clientService).findById(1L);
    }

    @Test
    void shouldCreateClient() throws Exception {

        CreateClientRequest request = validRequest();

        Client client = validClient();

        when(clientService.createClient(any(CreateClientRequest.class))).thenReturn(client);

        mockMvc.perform(post("/api/v1/clients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(clientService).createClient(any(CreateClientRequest.class));
    }

    @Test
    void shouldChangeClientName() throws Exception {

        ChangeClientRequest request = validChangeRequest();

        Client client = validClient();

        when(clientService.changeClientName(eq(1L), any(ChangeClientRequest.class))).thenReturn(client);

        mockMvc.perform(put("/api/v1/clients/{id}/name", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(clientService).changeClientName(eq(1L), any(ChangeClientRequest.class));
    }

    @Test
    void shouldActivateClient() throws Exception {

        mockMvc.perform(put("/api/v1/clients/{id}/activate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Client with id 1 was successfully activated"));

        verify(clientService).activate(1L);
    }
    //<Client with id 1 was sucessfully activated>
    //<Client with id 1 was successfully activated>

    @Test
    void shouldDeactivateClient() throws Exception {

        mockMvc.perform(put("/api/v1/clients/{id}/deactivate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Client with id 1 was successfully deactivated"));

        verify(clientService).deactivate(1L);
    }

    @Test
    void shouldReturn404WhenClientNotFound() throws Exception {

        when(clientService.findById(1L)).thenThrow(new ResourceNotFoundException("Client not found"));

        mockMvc.perform(get("/api/v1/clients/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Client not found"));

        verify(clientService).findById(1L);
    }

    @Test
    void shouldReturn400WhenBusinessExceptionOccurs() throws Exception {

        CreateClientRequest request = validRequest();

        when(clientService.createClient(any(CreateClientRequest.class)))
            .thenThrow(new BusinessException("Client with CPF already exists"));

        mockMvc.perform(post("/api/v1/clients", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client with CPF already exists"));

        verify(clientService).createClient(any(CreateClientRequest.class));
    }
}
