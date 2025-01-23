package com.template.spring.crud;

import com.template.spring.application.exception.UnknownEntityException;
import com.template.spring.application.mapper.EmployeeMapper;
import com.template.spring.infrastructure.persistence.dbo.EmployeeDBO;
import com.template.spring.web.dto.input.EmployeeDTO;
import com.template.spring.web.dto.input.EmployeePaginatedDto;
import com.template.spring.domain.model.Employee;

import com.template.spring.web.dto.output.EmployeeDTOResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CrudServiceImplTest {

    @Mock
    private CrudRepositoryImpl<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> repository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private CrudServiceImpl<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;


    @BeforeEach
    public void setUp() {
        employee = Employee.builder().name("David").email("david@gmail.com").phone("12354353").build();
        employeeDTO = EmployeeDTO.builder().name("David").email("david@gmail.com").phone("12354353").build();
        employeeService = new CrudServiceImpl<>(repository, mapper) {};
    }

    @Test
    public void testCreate() {
        when(mapper.DTOToEntity(employeeDTO)).thenReturn(employee);
        when(repository.save(employee)).thenReturn(employee);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO createdEmployee = employeeService.create(employeeDTO);

        assertNotNull(createdEmployee);
        assertEquals(employeeDTO.getName(), createdEmployee.getName());
        verify(repository).save(employee);
    }

    @Test
    public void testGetById() throws UnknownEntityException {
        when(repository.getById("1")).thenReturn(employee);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO foundEmployee = employeeService.getById("1");

        assertNotNull(foundEmployee);
        assertEquals(employeeDTO.getName(), foundEmployee.getName());
        verify(repository).getById("1");
    }

    @Test
    public void testUpdate() throws UnknownEntityException {
        when(mapper.DTOToEntity(employeeDTO)).thenReturn(employee);
        when(repository.update("1", employee)).thenReturn(employee);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO updatedEmployee = employeeService.update("1", employeeDTO);

        assertNotNull(updatedEmployee);
        assertEquals(employeeDTO.getName(), updatedEmployee.getName());
        verify(repository).update("1", employee);
    }

    @Test
    public void testGetAll() {
        List<Employee> employees = Arrays.asList(employee);
        List<EmployeeDTO> employeeDTOs = Arrays.asList(employeeDTO);

        when(repository.getAll()).thenReturn(employees);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> foundEmployees = employeeService.getAll();

        assertNotNull(foundEmployees);
        assertEquals(1, foundEmployees.size());
        verify(repository).getAll();
    }

    @Test
    public void testDelete() {
        doNothing().when(repository).delete("1");

        employeeService.delete("1");

        verify(repository).delete("1");
    }

    @Test
    public void testPatch() throws UnknownEntityException {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "John Doe Updated");

        when(repository.patch("1", updates)).thenReturn(employee);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO patchedEmployee = employeeService.patch("1", updates);

        assertNotNull(patchedEmployee);
        assertEquals(employeeDTO.getName(), patchedEmployee.getName());
        verify(repository).patch("1", updates);
    }

    @Test
    public void testGetPaginated() throws IllegalAccessException {
        EmployeePaginatedDto<EmployeeDTO> paginatedDto = new EmployeePaginatedDto<>();
        paginatedDto.setCurrentPage(0);
        paginatedDto.setPageSize(10);
        paginatedDto.setOrder(new EmployeePaginatedDto.Order("name", "asc"));
        paginatedDto.setSearchFields(employeeDTO);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<Employee> paginatedEntities = new PageImpl<>(Arrays.asList(employee), pageable, 1);

        when(mapper.DTOToEntity(employeeDTO)).thenReturn(employee);
        when(repository.findPaginated(employee, pageable)).thenReturn(paginatedEntities);
        when(mapper.EntityToDTO(employee)).thenReturn(employeeDTO);

        Page<EmployeeDTO> employeePage = employeeService.getPaginated(paginatedDto);

        assertNotNull(employeePage);
        assertEquals(1, employeePage.getTotalElements());
        verify(repository).findPaginated(employee, pageable);
    }



}