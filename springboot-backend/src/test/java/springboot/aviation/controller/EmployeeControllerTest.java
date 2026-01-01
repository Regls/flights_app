package springboot.aviation.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import springboot.aviation.model.Employee;
import springboot.aviation.repository.EmployeeRepository;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

	@Test
	void shouldReturnEmployeeList() throws Exception {
		Employee emp = new Employee("Renan", "Reginato", "renan.reginato@gmail.com");

		when(employeeRepository.findAll()).thenReturn(List.of(emp));

		mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].firstName").value("Renan")); 
	}

    @Test
    void shouldCreateEmployee() throws Exception {
        Employee emp = new Employee("Renan", "Reginato", "renan.reginato@gmail.com");

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.emailId").value("renan.reginato@gmail.com"));
    }

    @Test
    void shouldReturnEmployeeId() throws Exception {
        Employee emp = new Employee("Renan", "Reginato", "renan.reginato@gmail.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("Renan"));
    }

    @Test
    void shouldReturn404WhenEmployeeNotFound() throws Exception {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateEmployee() throws Exception {
        Employee existing = new Employee("Renan", "Old", "old@gmail.com");
        Employee updated = new Employee("Renan", "Reginato", "renan@gmail.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));

        when(employeeRepository.save(existing)).thenReturn(updated);

        mockMvc.perform(put("/api/v1/employees/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lastName").value("Reginato"));
    }

    @Test
    void shouldDelete() throws Exception {
        Employee emp = new Employee("Renan", "Reginato", "renan@gmail.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        doNothing().when(employeeRepository).delete(emp);

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.deleted").value(true));
    }
}
