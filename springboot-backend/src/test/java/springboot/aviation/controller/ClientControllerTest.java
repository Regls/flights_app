package springboot.aviation.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import springboot.aviation.model.Client;
import springboot.aviation.service.ClientService;
import java.util.List;

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

    @Test
    void shouldReturnClientList() throws Exception {
        
        Client client = validClient();
        
        when(clientService.findAll()).thenReturn(List.of(client));

        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        
        verify(clientService).findAll();
    }

}
