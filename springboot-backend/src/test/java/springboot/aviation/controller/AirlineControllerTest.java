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

import springboot.aviation.dto.ChangeAirlineRequest;
import springboot.aviation.dto.CreateAirlineRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Airline;
import springboot.aviation.service.AirlineService;


@WebMvcTest(AirlineController.class)
public class AirlineControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirlineService airlineService;

    @Autowired
    private ObjectMapper objectMapper;

    private Airline validAirline() {
        return Airline.createAirline("G3", "Gol Airlines");
    }

    private CreateAirlineRequest validRequest() {
        CreateAirlineRequest request = new CreateAirlineRequest();
        request.iataCode = "G3";
        request.airlineName = "Gol Airlines";
        return request;
    }

    private ChangeAirlineRequest validChangeRequest() {
        ChangeAirlineRequest request = new ChangeAirlineRequest();
        request.airlineName = "Gol LATAM Airlines";
        return request;
    }

    @Test
    void shouldReturnAirlineList() throws Exception {

        Airline airline = validAirline();

        when(airlineService.findAll()).thenReturn(List.of(airline));

        mockMvc.perform(get("/api/v1/airlines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(airlineService).findAll();
    }

    @Test
    void shouldReturnAirlineById() throws Exception {

        Airline airline = validAirline();

        when(airlineService.findById(1L)).thenReturn(airline);

        mockMvc.perform(get("/api/v1/airlines/{id}", 1L))
                .andExpect(status().isOk());

        verify(airlineService).findById(1L);
    }

    @Test
    void shouldCreateAirline() throws Exception {

        CreateAirlineRequest request = validRequest();

        Airline airline = validAirline();

        when(airlineService.createAirline(any(CreateAirlineRequest.class))).thenReturn(airline);

        mockMvc.perform(post("/api/v1/airlines")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(airlineService).createAirline(any(CreateAirlineRequest.class));
    }

    @Test
    void shouldChangeAirlineName() throws Exception {

        ChangeAirlineRequest request = validChangeRequest();

        Airline airline = validAirline();

        when(airlineService.changeAirlineName(eq(1L), any(ChangeAirlineRequest.class))).thenReturn(airline);

        mockMvc.perform(put("/api/v1/airlines/{id}/name", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(airlineService).changeAirlineName(eq(1L), any(ChangeAirlineRequest.class));
    }

    @Test
    void shouldOpenAirline() throws Exception {

        mockMvc.perform(put("/api/v1/airlines/{id}/activate", 1L))
                .andExpect(status().isNoContent());

        verify(airlineService).activate(1L);
    }

    @Test
    void shouldCloseAirline() throws Exception {

        mockMvc.perform(put("/api/v1/airlines/{id}/suspend", 1L))
                .andExpect(status().isNoContent());

        verify(airlineService).suspend(1L);
    }

    @Test
    void shouldReturn404WhenAirlineNotFound() throws Exception {

        when(airlineService.findById(1L)).thenThrow(new ResourceNotFoundException("Airline not found"));

        mockMvc.perform(get("/api/v1/airlines/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Airline not found"));

        verify(airlineService).findById(1L);
    }

    @Test
    void shouldReturn400WhenBusinessExceptionOccurs() throws Exception {

        CreateAirlineRequest request = validRequest();

        when(airlineService.createAirline(any(CreateAirlineRequest.class)))
                .thenThrow(new BusinessException("Airline with IATA code already exists."));

        mockMvc.perform(post("/api/v1/airlines", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Airline with IATA code already exists."));

        verify(airlineService).createAirline(any(CreateAirlineRequest.class));
    }
}
