package springboot.aviation.controller;

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

import springboot.aviation.dto.request.CreateBookingRequest;
import springboot.aviation.exception.BusinessException;
import springboot.aviation.exception.ResourceNotFoundException;
import springboot.aviation.model.Airline;
import springboot.aviation.model.Airport;
import springboot.aviation.model.Booking;
import springboot.aviation.model.Flight;
import springboot.aviation.model.Client;
import springboot.aviation.service.BookingService;


@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Booking validBooking() {
        return Booking.createBooking(
            Client.createClient("12345678900", "New", "Client"),
            Flight.createFlight(
                "G39206",
                Airline.createAirline("G3", "Gol Airlines"),
                Airport.createAirport("DPT", "Dep Airport", "Departure City"),
                Airport.createAirport("ARR", "Arr Airport", "Arrival City"),
                LocalDateTime.of(2024, 7, 1, 10, 0),
                LocalDateTime.of(2024, 7, 1, 12, 0)
            ));
    }

    private CreateBookingRequest validRequest() {
        CreateBookingRequest request = new CreateBookingRequest();
        request.clientId = 1L;
        request.flightId = 1L;
        return request;
    }

    @Test
    void shouldReturnBookingList() throws Exception {

        Booking booking = validBooking();

        when(bookingService.findAll()).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/v1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(bookingService).findAll();
    }

    @Test
    void shouldReturnBookingById() throws Exception {

        Booking booking = validBooking();

        when(bookingService.findById(1L)).thenReturn(booking);

        mockMvc.perform(get("/api/v1/bookings/{id}", 1L))
                .andExpect(status().isOk());

        verify(bookingService).findById(1L);
    }

    @Test
    void shouldCreateBooking() throws Exception {

        CreateBookingRequest request = validRequest();

        Booking booking = validBooking();

        when(bookingService.createBooking(any(CreateBookingRequest.class))).thenReturn(booking);

        mockMvc.perform(post("/api/v1/bookings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(bookingService).createBooking(any(CreateBookingRequest.class));
    }

    @Test
    void shouldDepartBooking() throws Exception {

        mockMvc.perform(put("/api/v1/bookings/{id}/confirm", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Booking with id 1 was successfully confirmed"));

        verify(bookingService).confirm(1L);
    }

    @Test
    void shouldCancelBooking() throws Exception {

        mockMvc.perform(put("/api/v1/bookings/{id}/cancel", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Booking with id 1 was successfully cancelled"));

        verify(bookingService).cancel(1L);
    }

    @Test
    void shouldReturn404WhenBookingNotFound() throws Exception {

        when(bookingService.findById(1L)).thenThrow(new ResourceNotFoundException("Booking not found"));

        mockMvc.perform(get("/api/v1/bookings/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Booking not found"));

        verify(bookingService).findById(1L);
    }

    @Test
    void shouldReturn400WhenBusinessExceptionOccurs() throws Exception {

        CreateBookingRequest request = validRequest();

        when(bookingService.createBooking(any(CreateBookingRequest.class)))
                .thenThrow(new BusinessException("Client already has an active booking for this flight"));

        mockMvc.perform(post("/api/v1/bookings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Client already has an active booking for this flight"));

        verify(bookingService).createBooking(any(CreateBookingRequest.class));
    }

}

