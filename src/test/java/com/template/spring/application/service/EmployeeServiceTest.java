package com.template.spring.application.service;

import com.template.spring.application.exception.UnknownEntityException;
import com.template.spring.application.mapper.EmployeeMapper;
import com.template.spring.application.service.CrudRepositoryImpl;
import com.template.spring.application.service.CrudServiceImpl;
import com.template.spring.infrastructure.persistence.dbo.EmployeeDBO;
import com.template.spring.web.dto.input.EmployeeDTO;
import com.template.spring.web.dto.input.EmployeePaginatedDto;
import com.template.spring.domain.model.Employee;

import com.template.spring.web.dto.output.EmployeeDTOResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Stream;

import static com.template.spring.utils.TestParametersProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @Mock
    private CrudRepositoryImpl<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> repository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private CrudServiceImpl<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> employeeService;


    @BeforeEach
    public void setUp() {
        employeeService = new CrudServiceImpl<>(repository, mapper) {};
    }


    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testCreate(Employee employee, EmployeeDTO employeeDTO) {
        when(mapper.DTOToEntity(employeeDTO)).thenReturn(employee);
        when(repository.save(employee)).thenReturn(employee);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO createdEmployee = employeeService.create(employeeDTO);

        assertNotNull(createdEmployee);
        assertEquals(employeeDTO.getName(), createdEmployee.getName());
        verify(repository).save(employee);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testGetById(Employee employee, EmployeeDTO employeeDTO, String id) throws UnknownEntityException {
        when(repository.getById(id)).thenReturn(employee);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO foundEmployee = employeeService.getById(id);

        assertNotNull(foundEmployee);
        assertEquals(employeeDTO.getName(), foundEmployee.getName());
        verify(repository).getById(id);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testUpdate(Employee employee, EmployeeDTO employeeDTO, String id) throws UnknownEntityException {
        when(mapper.DTOToEntity(employeeDTO)).thenReturn(employee);
        when(repository.update(id, employee)).thenReturn(employee);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO updatedEmployee = employeeService.update(id, employeeDTO);

        assertNotNull(updatedEmployee);
        assertEquals(employeeDTO.getName(), updatedEmployee.getName());
        verify(repository).update(id, employee);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testGetAll(Employee employee, EmployeeDTO employeeDTO) {
        List<Employee> employees = Collections.singletonList(employee);

        when(repository.getAll()).thenReturn(employees);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> foundEmployees = employeeService.getAll();

        assertNotNull(foundEmployees);
        assertEquals(1, foundEmployees.size());
        assertEquals(employeeDTO.getName(), foundEmployees.get(0).getName());
        verify(repository).getAll();
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testDelete(Employee employee, EmployeeDTO employeeDTO, String id) {
        doNothing().when(repository).delete(id);

        employeeService.delete(id);

        verify(repository).delete(id);
    }

    @ParameterizedTest
    @MethodSource("providePatchTestCases")
    public void testPatch(Employee employee, EmployeeDTO employeeDTO, Map<String, Object> updates, String id) throws UnknownEntityException {
        when(repository.patch(id, updates)).thenReturn(employee);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO patchedEmployee = employeeService.patch(id, updates);

        assertNotNull(patchedEmployee);
        assertEquals(employeeDTO.getName(), patchedEmployee.getName());
        verify(repository).patch(id, updates);
    }

    @ParameterizedTest
    @MethodSource("providePaginatedTestCases")
    public void testGetPaginated(Employee employee, EmployeeDTO employeeDTO, Pageable pageable, Page<Employee> paginatedEntities) throws IllegalAccessException {
        EmployeePaginatedDto<EmployeeDTO> paginatedDto = new EmployeePaginatedDto<>();
        paginatedDto.setCurrentPage(pageable.getPageNumber());
        paginatedDto.setPageSize(pageable.getPageSize());
        paginatedDto.setOrder(new EmployeePaginatedDto.Order("name", "asc"));
        paginatedDto.setSearchFields(employeeDTO);

        when(mapper.DTOToEntity(employeeDTO)).thenReturn(employee);
        when(repository.findPaginated(employee, pageable)).thenReturn(paginatedEntities);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        Page<EmployeeDTO> employeePage = employeeService.getPaginated(paginatedDto);

        assertNotNull(employeePage);
        assertEquals(paginatedEntities.getTotalElements(), employeePage.getTotalElements());
        verify(repository).findPaginated(employee, pageable);
    }


    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(EMPLOYEE1, EMPLOYEE_DTO1, "41fbfe88-ea44-4606-a565-858c6e3b2e9c"),
                Arguments.of(EMPLOYEE2, EMPLOYEE_DTO2, "92124e84-sa14-4n34-a452-858346457457")
        );
    }

    private static Stream<Arguments> providePatchTestCases() {

        Map<String, Object> updates = Map.of("name", "John Updated", "email", "updated@example.com");

        return Stream.of(
                Arguments.of(EMPLOYEE1, EMPLOYEE_DTO1, updates, "41fbfe88-ea44-4606-a565-858c6e3b2e9c"),
                Arguments.of(EMPLOYEE2, EMPLOYEE_DTO2, updates, "92124e84-sa14-4n34-a452-858346457457")
        );
    }

    private static Stream<Arguments> providePaginatedTestCases() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Employee> paginatedEntities = new PageImpl<>(List.of(EMPLOYEE1), pageable, 1);

        return Stream.of(
                Arguments.of(EMPLOYEE1, EMPLOYEE_DTO1, pageable, paginatedEntities),
                Arguments.of(EMPLOYEE2, EMPLOYEE_DTO2, pageable, paginatedEntities)
        );
    }


}