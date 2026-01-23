/*package springboot.aviation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import springboot.aviation.dto.request.CreateFlightRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Airline;
import springboot.aviation.model.Airport;
import springboot.aviation.model.Flight;
import springboot.aviation.service.FlightService;


@WebMvcTest(FlightController.class)
public class FlightControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;

    private Flight validFlight() {
        return Flight.createFlight(
            "G39206",
            Airline.createAirline("G3", "Gol Airlines"),
            Airport.createAirport("DPT", "Dep Airport", "Departure City"),
            Airport.createAirport("ARR", "Arr Airport", "Arrival City"),
            LocalDateTime.of(2024, 7, 1, 10, 0),
            LocalDateTime.of(2024, 7, 1, 12, 0)
        );
    }

    private CreateFlightRequest validRequest() {
        CreateFlightRequest request = new CreateFlightRequest();
        request.flightNumber = "G39206";
        request.airlineId = 1L;
        request.departureAirportId = 1L;
        request.arrivalAirportId = 2L;
        request.departureTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        request.arrivalTime = LocalDateTime.of(2024, 1, 1, 12, 0);
        return request;
    }

    @Test
    void shouldReturnFlightList() throws Exception {

        Flight flight = validFlight();

        when(flightService.findAll()).thenReturn(List.of(flight));

        mockMvc.perform(get("/api/v1/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(flightService).findAll();
    }

    @Test
    void shouldReturnFlightById() throws Exception {

        Flight flight = validFlight();

        when(flightService.findById(1L)).thenReturn(flight);

        mockMvc.perform(get("/api/v1/flights/{id}", 1L))
                .andExpect(status().isOk());

        verify(flightService).findById(1L);
    }

    @Test
    void shouldCreateFlight() throws Exception {

        CreateFlightRequest request = validRequest();

        Flight flight = validFlight();

        when(flightService.createFlight(any(CreateFlightRequest.class))).thenReturn(flight);

        mockMvc.perform(post("/api/v1/flights")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(flightService).createFlight(any(CreateFlightRequest.class));
    }

    @Test
    void shouldDepartFlight() throws Exception {

        mockMvc.perform(put("/api/v1/flights/{id}/depart", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Flight with id 1 was successfully departed"));

        verify(flightService).depart(1L);
    }

    @Test
    void shouldArriveFlight() throws Exception {

        mockMvc.perform(put("/api/v1/flights/{id}/arrive", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Flight with id 1 was successfully arrived"));

        verify(flightService).arrive(1L);
    }

    @Test
    void shouldCancelFlight() throws Exception {

        mockMvc.perform(put("/api/v1/flights/{id}/cancel", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Flight with id 1 was successfully cancelled"));

        verify(flightService).cancel(1L);
    }

    @Test
    void shouldReturn404WhenFlightNotFound() throws Exception {

        when(flightService.findById(1L)).thenThrow(new ResourceNotFoundException("Flight not found"));

        mockMvc.perform(get("/api/v1/flights/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Flight not found"));

        verify(flightService).findById(1L);
    }

    @Test
    void shouldReturn400WhenBusinessExceptionOccurs() throws Exception {

        CreateFlightRequest request = validRequest();

        when(flightService.createFlight(any(CreateFlightRequest.class)))
                .thenThrow(new BusinessException("Flight with flight number already exists"));

        mockMvc.perform(post("/api/v1/flights")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Flight with flight number already exists"));

        verify(flightService).createFlight(any(CreateFlightRequest.class));
    }
}
*/
