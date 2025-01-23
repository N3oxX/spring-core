package com.template.spring.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.spring.application.exception.UnknownEntityException;
import com.template.spring.application.mapper.EmployeeMapper;
import com.template.spring.application.service.EmployeeService;
import com.template.spring.domain.model.Employee;
import com.template.spring.web.dto.input.EmployeeDTO;
import com.template.spring.web.dto.input.EmployeePaginatedDto;
import com.template.spring.web.dto.output.EmployeeDTOResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @MockBean
    private EmployeeMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateEmployee() throws Exception {
        // Prepare mock data
        EmployeeDTO inputDto = new EmployeeDTO(UUID.randomUUID().toString(), "David", "david@gmail.com", "12354353");
        EmployeeDTOResponse responseDto =  EmployeeDTOResponse.builder().name("David").email("david@gmail.com").phone("12354353").build();

        // Mock behavior
        Mockito.when(service.create(any(EmployeeDTO.class))).thenReturn(inputDto);
        Mockito.when(mapper.DTOToResponse(inputDto)).thenReturn(responseDto);

        // Perform POST request
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("David"))
                .andExpect(jsonPath("$.email").value("david@gmail.com"))
                .andExpect(jsonPath("$.phone").value("12354353"));
    }

    @Test
    public void testCreateEmployeeWithInvalidEmail() throws Exception {
        // Prepare mock data with invalid email format
        EmployeeDTO invalidEmailDto = new EmployeeDTO(UUID.randomUUID().toString(), "David", "invalid-email", "12354353");

        // Perform POST request with invalid email
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("email"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("Email should be valid"));
    }

    @Test
    public void testFindAllEmployees() throws Exception {
        // Prepare mock data
        List<EmployeeDTO> employeeList = List.of(
                new EmployeeDTO(UUID.randomUUID().toString(), "David", "david@gmail.com", "12354353"),
                new EmployeeDTO(UUID.randomUUID().toString(), "John", "john@gmail.com", "98765432")
        );

        List<EmployeeDTOResponse> responseList = employeeList.stream()
                .map(emp -> EmployeeDTOResponse.builder()
                        .name(emp.getName())
                        .email(emp.getEmail())
                        .phone(emp.getPhone())
                        .build())
                .collect(Collectors.toList());

        // Mock service and mapper behavior
        Mockito.when(service.getAll()).thenReturn(employeeList);
        Mockito.when(mapper.DTOToResponse(any(EmployeeDTO.class))).thenAnswer(invocation -> {
            EmployeeDTO emp = invocation.getArgument(0);
            return EmployeeDTOResponse.builder()
                    .name(emp.getName())
                    .email(emp.getEmail())
                    .phone(emp.getPhone())
                    .build();
        });

        // Perform GET request
        mockMvc.perform(get("/employees/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("David"))
                .andExpect(jsonPath("$[0].email").value("david@gmail.com"))
                .andExpect(jsonPath("$[0].phone").value("12354353"))
                .andExpect(jsonPath("$[1].name").value("John"))
                .andExpect(jsonPath("$[1].email").value("john@gmail.com"))
                .andExpect(jsonPath("$[1].phone").value("98765432"));
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        String id = UUID.randomUUID().toString();
        EmployeeDTO inputDto = new EmployeeDTO(id, "David", "david@gmail.com", "12354353");
        EmployeeDTOResponse responseDto = EmployeeDTOResponse.builder()
                .name("David")
                .email("david@gmail.com")
                .phone("12354353")
                .build();

        Mockito.when(service.getById(eq(id))).thenReturn(inputDto);
        Mockito.when(mapper.DTOToResponse(inputDto)).thenReturn(responseDto);

        mockMvc.perform(get("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("David"))
                .andExpect(jsonPath("$.email").value("david@gmail.com"))
                .andExpect(jsonPath("$.phone").value("12354353"));
    }

    @Test
    public void testGetEmployeeById_NotFound() throws Exception {
        String id = UUID.randomUUID().toString();
        String errorMessage = "Employee not found";

        Mockito.when(service.getById(eq(id))).thenThrow(new UnknownEntityException(errorMessage));

        mockMvc.perform(get("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.details").value("The requested employee could not be found."));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        String id = UUID.randomUUID().toString();
        EmployeeDTO inputDto = new EmployeeDTO(id, "David Updated", "david.updated@gmail.com", "98765432");
        EmployeeDTOResponse responseDto = EmployeeDTOResponse.builder()
                .name("David Updated")
                .email("david.updated@gmail.com")
                .phone("98765432")
                .build();

        Mockito.when(service.update(eq(id), any(EmployeeDTO.class))).thenReturn(inputDto);
        Mockito.when(mapper.DTOToResponse(inputDto)).thenReturn(responseDto);

        mockMvc.perform(put("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("David Updated"))
                .andExpect(jsonPath("$.email").value("david.updated@gmail.com"))
                .andExpect(jsonPath("$.phone").value("98765432"));
    }

    @Test
    public void testUpdateEmployee_InvalidField() throws Exception {
        String id = UUID.randomUUID().toString();
        EmployeeDTO inputDto = new EmployeeDTO(id, "David Updated", "david.updated@gmail.com", "98765432");
        String errorMessage = "Invalid field provided";

        // Mock behavior to throw IllegalArgumentException
        Mockito.when(service.update(eq(id), any(EmployeeDTO.class))).thenThrow(new IllegalArgumentException(errorMessage));

        // Perform PUT request and expect IllegalArgumentException
        mockMvc.perform(put("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.details").value("The field don't exist."));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        String id = UUID.randomUUID().toString();

        mockMvc.perform(delete("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).delete(eq(id));
    }

    @Test
    public void testPatchEmployee() throws Exception {
        String id = UUID.randomUUID().toString();
        Map<String, Object> updates = Map.of("name", "David Patched");
        EmployeeDTO updatedDto = new EmployeeDTO(id, "David Patched", "david@gmail.com", "12354353");
        EmployeeDTOResponse responseDto = EmployeeDTOResponse.builder()
                .name("David Patched")
                .email("david@gmail.com")
                .phone("12354353")
                .build();

        Mockito.when(service.patch(eq(id), any(Map.class))).thenReturn(updatedDto);
        Mockito.when(mapper.DTOToResponse(updatedDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("David Patched"))
                .andExpect(jsonPath("$.email").value("david@gmail.com"))
                .andExpect(jsonPath("$.phone").value("12354353"));
    }

    @Test
    public void testGetPaginatedEmployees() throws Exception {
        EmployeeDTO searchFields = new EmployeeDTO(null, "David", null, null);
        EmployeePaginatedDto<EmployeeDTO> paginatedDto = new EmployeePaginatedDto<>();
        paginatedDto.setCurrentPage(0);
        paginatedDto.setPageSize(5);
        paginatedDto.setOrder(new EmployeePaginatedDto.Order("name", "ASC"));
        paginatedDto.setSearchFields(searchFields);

        List<EmployeeDTO> employees = List.of(
                new EmployeeDTO(UUID.randomUUID().toString(), "David", "david@gmail.com", "12354353"),
                new EmployeeDTO(UUID.randomUUID().toString(), "John", "john@gmail.com", "45678901")
        );

        Page<EmployeeDTO> employeePage = new PageImpl<>(employees, PageRequest.of(0, 5, Sort.by("name")), employees.size());
        Page<EmployeeDTOResponse> responsePage = employeePage.map(emp -> EmployeeDTOResponse.builder()
                .name(emp.getName())
                .email(emp.getEmail())
                .phone(emp.getPhone())
                .build());

        Mockito.when(service.getPaginated(any(EmployeePaginatedDto.class))).thenReturn(employeePage);
        Mockito.when(mapper.DTOToResponse(any(EmployeeDTO.class))).thenAnswer(invocation -> {
            EmployeeDTO emp = invocation.getArgument(0);
            return EmployeeDTOResponse.builder()
                    .name(emp.getName())
                    .email(emp.getEmail())
                    .phone(emp.getPhone())
                    .build();
        });

        mockMvc.perform(post("/employees/paginated")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paginatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("David"))
                .andExpect(jsonPath("$.content[0].email").value("david@gmail.com"))
                .andExpect(jsonPath("$.content[1].name").value("John"))
                .andExpect(jsonPath("$.content[1].email").value("john@gmail.com"));
    }


    @Test
    public void testGetPaginatedEmployees_AccesException() throws Exception {
        EmployeeDTO searchFields = new EmployeeDTO(null, "David", null, null);
        EmployeePaginatedDto<EmployeeDTO> paginatedDto = new EmployeePaginatedDto<>();
        paginatedDto.setCurrentPage(0);
        paginatedDto.setPageSize(5);
        paginatedDto.setOrder(new EmployeePaginatedDto.Order("name", "ASC"));
        paginatedDto.setSearchFields(searchFields);

        List<EmployeeDTO> employees = List.of(
                new EmployeeDTO(UUID.randomUUID().toString(), "David", "david@gmail.com", "12354353"),
                new EmployeeDTO(UUID.randomUUID().toString(), "John", "john@gmail.com", "45678901")
        );

        Page<EmployeeDTO> employeePage = new PageImpl<>(employees, PageRequest.of(0, 5, Sort.by("name")), employees.size());

        String errorMessage = "Invalid field provided";

        Mockito.when(service.getPaginated(any(EmployeePaginatedDto.class))).thenThrow(new IllegalAccessException(errorMessage));
        Mockito.when(mapper.DTOToResponse(any(EmployeeDTO.class))).thenAnswer(invocation -> {
            EmployeeDTO emp = invocation.getArgument(0);
            return EmployeeDTOResponse.builder()
                    .name(emp.getName())
                    .email(emp.getEmail())
                    .phone(emp.getPhone())
                    .build();
        });


        mockMvc.perform(post("/employees/paginated")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paginatedDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.details").value("Access to the field is not allowed."));
    }


}