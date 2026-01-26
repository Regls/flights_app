/*package springboot.aviation.controller;

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

import springboot.aviation.dto.request.ChangeAirportRequest;
import springboot.aviation.dto.request.CreateAirportRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Airport;
import springboot.aviation.service.AirportService;


@WebMvcTest(AirportController.class)
public class AirportControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirportService airportService;

    @Autowired
    private ObjectMapper objectMapper;

    private Airport validAirport() {
        return Airport.createAirport("GRU", "Guarulhos International Airport", "São Paulo");
    }

    private CreateAirportRequest validRequest() {
        CreateAirportRequest request = new CreateAirportRequest();
        request.iataCode = "GRU";
        request.airportName = "Guarulhos International Airport";
        request.city = "São Paulo";
        return request;
    }

    private ChangeAirportRequest validChangeRequest() {
        ChangeAirportRequest request = new ChangeAirportRequest();
        request.airportName = "Guarulhos National Airport";
        return request;
    }

    @Test
    void shouldReturnAirportList() throws Exception {

        Airport airport = validAirport();

        when(airportService.findAll()).thenReturn(List.of(airport));

        mockMvc.perform(get("/api/v1/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(airportService).findAll();
    }

    @Test
    void shouldReturnAirportById() throws Exception {

        Airport airport = validAirport();

        when(airportService.findById(1L)).thenReturn(airport);

        mockMvc.perform(get("/api/v1/airports/{id}", 1L))
                .andExpect(status().isOk());

        verify(airportService).findById(1L);
    }

    @Test
    void shouldCreateAirport() throws Exception {

        CreateAirportRequest request = validRequest();

        Airport airport = validAirport();

        when(airportService.createAirport(any(CreateAirportRequest.class))).thenReturn(airport);

        mockMvc.perform(post("/api/v1/airports")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(airportService).createAirport(any(CreateAirportRequest.class));
    }

    @Test
    void shouldChangeAirportName() throws Exception {

        ChangeAirportRequest request = validChangeRequest();

        Airport airport = validAirport();

        when(airportService.changeAirportName(eq(1L), any(ChangeAirportRequest.class))).thenReturn(airport);

        mockMvc.perform(put("/api/v1/airports/{id}/name", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(airportService).changeAirportName(eq(1L), any(ChangeAirportRequest.class));
    }

    @Test
    void shouldOpenAirport() throws Exception {

        mockMvc.perform(put("/api/v1/airports/{id}/open", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Airport with id 1 was successfully opened"));

        verify(airportService).open(1L);
    }

    @Test
    void shouldCloseAirport() throws Exception {

        mockMvc.perform(put("/api/v1/airports/{id}/close", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Airport with id 1 was successfully closed"));

        verify(airportService).close(1L);
    }

    @Test
    void shouldReturn404WhenAirportNotFound() throws Exception {

        when(airportService.findById(1L)).thenThrow(new ResourceNotFoundException("Airport not found"));

        mockMvc.perform(get("/api/v1/airports/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Airport not found"));

        verify(airportService).findById(1L);
    }

    @Test
    void shouldReturn400WhenBusinessExceptionOccurs() throws Exception {

        CreateAirportRequest request = validRequest();

        when(airportService.createAirport(any(CreateAirportRequest.class)))
                .thenThrow(new BusinessException("Airport with IATA code already exists"));

        mockMvc.perform(post("/api/v1/airports", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Airport with IATA code already exists"));

        verify(airportService).createAirport(any(CreateAirportRequest.class));
    }
}
*/