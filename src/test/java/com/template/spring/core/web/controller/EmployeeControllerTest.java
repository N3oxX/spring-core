package com.template.spring.core.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.spring.core.application.exception.UnknownEntityException;
import com.template.spring.core.application.mapper.EmployeeMapper;
import com.template.spring.core.application.service.EmployeeService;
import com.template.spring.core.web.dto.input.EmployeeDTO;
import com.template.spring.core.web.dto.input.PaginatedDTO;
import com.template.spring.core.web.dto.output.EmployeeDTOResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static com.template.spring.utils.EmployeeParametersProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @MockBean
    @Qualifier("employeeMapperImpl")
    private EmployeeMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static Stream<Arguments> provideInvalidEmailData() {
        return Stream.of(
                Arguments.of(
                        EMPLOYEE_EMAIL_WRONG1
                ),
                Arguments.of(
                        EMPLOYEE_EMAIL_WRONG2
                )
        );
    }

    private static Stream<Arguments> provideEmployeeData() {
        return Stream.of(
                Arguments.of(
                        "92124e84-sa14-4n34-a452-858346457457",
                        EMPLOYEE_DTO1,
                        EMPLOYEE_RESPONSE_DTO1
                ),
                Arguments.of(
                        "41fbfe88-ea44-4606-a565-858c6e3b2e9c",
                        EMPLOYEE_DTO2,
                        EMPLOYEE_RESPONSE_DTO2
                )
        );
    }

    private static Stream<Arguments> provideEmployeeDataPatch() {
        return Stream.of(
                Arguments.of(
                        "92124e84-sa14-4n34-a452-858346457457",
                        EMPLOYEE_DTO1,
                        EMPLOYEE_RESPONSE_DTO_PATCH1

                ),
                Arguments.of(
                        "41fbfe88-ea44-4606-a565-858c6e3b2e9c",
                        EMPLOYEE_DTO2,
                        EMPLOYEE_RESPONSE_DTO_PATCH2
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeData")
    public void testCreateEmployee(String ignoredId, EmployeeDTO inputDto, EmployeeDTOResponse responseDto) throws Exception {

        Mockito.when(service.create(any(EmployeeDTO.class))).thenReturn(inputDto);
        Mockito.when(mapper.DTOToResponse(inputDto)).thenReturn(responseDto);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(responseDto.name()))
                .andExpect(jsonPath("$.email").value(responseDto.email()))
                .andExpect(jsonPath("$.phone").value(responseDto.phone()));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEmailData")
    public void testCreateEmployeeWithInvalidEmail(EmployeeDTO invEmployeeDTO) throws Exception {
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invEmployeeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("email"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("Email should be valid"));
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeData")
    public void testUpdateEmployee(String id, EmployeeDTO inputDto, EmployeeDTOResponse responseDto) throws Exception {

        Mockito.when(service.update(eq(id), any(EmployeeDTO.class))).thenReturn(inputDto);
        Mockito.when(mapper.DTOToResponse(inputDto)).thenReturn(responseDto);

        mockMvc.perform(put("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(responseDto.name()))
                .andExpect(jsonPath("$.email").value(responseDto.email()))
                .andExpect(jsonPath("$.phone").value(responseDto.phone()));
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeData")
    public void testFindAllEmployees(String ignoredId, EmployeeDTO inputDto, EmployeeDTOResponse ignoredResponseDto) throws Exception {
        List<EmployeeDTO> employeeList = List.of(
                inputDto);

        Mockito.when(service.getAll()).thenReturn(employeeList);
        mockMapperDtoToResponse();

        mockMvc.perform(get("/employees/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(inputDto.getName()))
                .andExpect(jsonPath("$[0].email").value(inputDto.getEmail()))
                .andExpect(jsonPath("$[0].phone").value(inputDto.getPhone()));
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeData")
    public void testGetEmployeeById(String id, EmployeeDTO dto, EmployeeDTOResponse responseDto) throws Exception {

        Mockito.when(service.getById(eq(id))).thenReturn(dto);
        Mockito.when(mapper.DTOToResponse(dto)).thenReturn(responseDto);

        mockMvc.perform(get("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.phone").value(dto.getPhone()));
    }

    @Test
    public void testGetEmployeeById_NotFound() throws Exception {
        String id = UUID.randomUUID().toString();
        String errorMessage = "entity not found";

        Mockito.when(service.getById(eq(id))).thenThrow(new UnknownEntityException(errorMessage));

        mockMvc.perform(get("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.details").value("The requested entity could not be found."));
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeData")
    public void testUpdateEmployee_InvalidField(String id, EmployeeDTO dto, EmployeeDTOResponse ignoredResponseDto) throws Exception {
        String errorMessage = "Invalid field provided";


        Mockito.when(service.update(eq(id), any(EmployeeDTO.class))).thenThrow(new IllegalArgumentException(errorMessage));


        mockMvc.perform(put("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
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

    @ParameterizedTest
    @MethodSource("provideEmployeeDataPatch")
    public void testPatchEmployee(String id, EmployeeDTO dto, EmployeeDTOResponse responseDto) throws Exception {
        Map<String, Object> updates = Map.of("name", dto.getName() + " patched");
        EmployeeDTO updatedDto = new EmployeeDTO(id, dto.getName() + " patched", "david@gmail.com", "12354353");

        Mockito.when(service.patch(eq(id), anyMap())).thenReturn(updatedDto);
        Mockito.when(mapper.DTOToResponse(updatedDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedDto.getName()));
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeData")
    public void testGetPaginatedEmployees(String ignoredId, EmployeeDTO dto, EmployeeDTOResponse ignoredResponseDto) throws Exception {
        EmployeeDTO searchFields = new EmployeeDTO(null, "David", null, null);
        var paginatedDto = new PaginatedDTO<EmployeeDTO>();
        paginatedDto.setCurrentPage(0);
        paginatedDto.setPageSize(5);
        paginatedDto.setOrder(new PaginatedDTO.Order("name", "ASC"));
        paginatedDto.setSearchFields(searchFields);
        List<EmployeeDTO> employees = List.of(
                dto
        );
        Page<EmployeeDTO> employeePage = new PageImpl<>(employees, PageRequest.of(0, 5, Sort.by("name")), employees.size());

        Mockito.when(service.getPaginated(any())).thenReturn(employeePage);
        mockMapperDtoToResponse();

        mockMvc.perform(post("/employees/paginated")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paginatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(dto.getName()))
                .andExpect(jsonPath("$.content[0].email").value(dto.getEmail()));
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeData")
    public void testGetPaginatedEmployees_AccessException(String ignoredId, EmployeeDTO dto, EmployeeDTOResponse ignoredResponseDto) throws Exception {

        PaginatedDTO<EmployeeDTO> paginatedDto = new PaginatedDTO<>();
        paginatedDto.setCurrentPage(0);
        paginatedDto.setPageSize(5);
        paginatedDto.setOrder(new PaginatedDTO.Order("name", "ASC"));
        paginatedDto.setSearchFields(dto);

        String errorMessage = "Invalid field provided";

        Mockito.when(service.getPaginated(any())).thenThrow(new IllegalAccessException(errorMessage));
        mockMapperDtoToResponse();

        mockMvc.perform(post("/employees/paginated")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paginatedDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.details").value("Access to the field is not allowed."));
    }

    private void mockMapperDtoToResponse() {
        Mockito.when(mapper.DTOToResponse(any(EmployeeDTO.class))).thenAnswer(invocation -> {
            EmployeeDTO emp = invocation.getArgument(0);
            return EmployeeDTOResponse.builder()
                    .name(emp.getName())
                    .email(emp.getEmail())
                    .phone(emp.getPhone())
                    .build();
        });
    }
}
