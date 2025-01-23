package com.template.spring.infrastructure.persistence.repository;


import com.template.spring.application.exception.UnknownEntityException;
import com.template.spring.application.mapper.EmployeeMapper;
import com.template.spring.domain.model.Employee;

import com.template.spring.infrastructure.persistence.dbo.EmployeeDBO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class EmployeeRepositoryTest {

    @Mock
    private EmployeeJpaRepository repository;

    @Mock
    private EmployeeMapper mapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EmployeeRepository employeeRepository;

    private Employee employee;

    private EmployeeDBO employeeDBO;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder().name("David").email("david@gmail.com").phone("12354353").build();
        employeeDBO = new EmployeeDBO("David","david@gmail.com","12354353");

        employeeRepository = new EmployeeRepository(repository, mapper, entityManager);
    }

    @Test
    public void testSave() {
        when(mapper.EntityToDBO(employee)).thenReturn(employeeDBO);
        when(repository.save(employeeDBO)).thenReturn(employeeDBO);
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        Employee savedEmployee = employeeRepository.save(employee);

        assertNotNull(savedEmployee);
        assertEquals(employee.getName(), savedEmployee.getName());
        verify(repository).save(employeeDBO);
    }

    @Test
    public void testGetById() throws UnknownEntityException {
        when(repository.findById("1")).thenReturn(Optional.of(employeeDBO));
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        Employee foundEmployee = employeeRepository.getById("1");

        assertNotNull(foundEmployee);
        assertEquals(employee.getName(), foundEmployee.getName());
        verify(repository).findById("1");
    }

    @Test
    public void testGetByIdThrowsUnknownEntityException() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UnknownEntityException.class, () -> {
            employeeRepository.getById("1");
        });
    }

    @Test
    public void testUpdate() throws UnknownEntityException {
        when(repository.findById("1")).thenReturn(Optional.of(employeeDBO));
        doNothing().when(mapper).updateDBOFromEntity(employee, employeeDBO);
        when(repository.save(employeeDBO)).thenReturn(employeeDBO);
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        Employee updatedEmployee = employeeRepository.update("1", employee);

        assertNotNull(updatedEmployee);
        assertEquals(employee.getName(), updatedEmployee.getName());
        verify(repository).findById("1");
        verify(repository).save(employeeDBO);
    }

    @Test
    public void testUpdateThrowsUnknownEntityException() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UnknownEntityException.class, () -> {
            employeeRepository.update("1", employee);
        });
    }


    @Test
    public void testGetAll() {
        List<EmployeeDBO> employeeDBOs = Arrays.asList(employeeDBO);
        when(repository.findAll()).thenReturn(employeeDBOs);
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        List<Employee> employees = employeeRepository.getAll();

        assertNotNull(employees);
        assertEquals(1, employees.size());
        verify(repository).findAll();
    }

    @Test
    public void testDelete() {
        doNothing().when(repository).deleteById("1");

        employeeRepository.delete("1");

        verify(repository).deleteById("1");
    }

    @Test
    public void testPatch() throws UnknownEntityException {
        when(repository.findById("1")).thenReturn(Optional.of(employeeDBO));
        when(repository.save(employeeDBO)).thenReturn(employeeDBO);
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "John Doe Updated");

        Employee patchedEmployee = employeeRepository.patch("1", updates);

        assertNotNull(patchedEmployee);
        assertEquals("David", patchedEmployee.getName());
        verify(repository).findById("1");
        verify(repository).save(employeeDBO);
    }

    @Test
    public void testPatchThrowsUnknownEntityException() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "John Doe Updated");

        assertThrows(UnknownEntityException.class, () -> {
            employeeRepository.patch("1", updates);
        });
    }

    @Test
    public void testPatchThrowsIllegalArgumentException() throws UnknownEntityException {
        when(repository.findById("1")).thenReturn(Optional.of(employeeDBO));

        Map<String, Object> updates = new HashMap<>();
        updates.put("id", "newId"); // Trying to update the 'id' field should throw IllegalArgumentException

        assertThrows(IllegalArgumentException.class, () -> {
            employeeRepository.patch("1", updates);
        });
    }

    @Test
    public void testPatchThrowsIllegalArgumentExceptionOnFieldSetFailure() throws UnknownEntityException {
        // Arrange
        String id = "1";
        Map<String, Object> updates = new HashMap<>();
        updates.put("nonExistentField", "newValue");

        when(repository.findById(id)).thenReturn(Optional.of(employeeDBO));

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeRepository.patch(id, updates);
        });

        assertTrue(exception.getMessage().contains("Failed to set field: nonExistentField"));

        // Verify
        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testFindPaginated_withNullSearchFields() throws IllegalAccessException {
        // Arrange: Mock dependencies
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<EmployeeDBO> criteriaQuery = mock(CriteriaQuery.class);
        Root<EmployeeDBO> root = mock(Root.class);
        TypedQuery<EmployeeDBO> typedQuery = mock(TypedQuery.class);
        CriteriaQuery<Long> countQuery = mock(CriteriaQuery.class);
        TypedQuery<Long> countTypedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(EmployeeDBO.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(EmployeeDBO.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(employeeDBO));

        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        when(countQuery.select(criteriaBuilder.count(countQuery.from(EmployeeDBO.class)))).thenReturn(countQuery);
        when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
        when(countTypedQuery.getSingleResult()).thenReturn(1L);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Employee> result = employeeRepository.findPaginated(null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testFindPaginated_withNullFieldsInSearchFields() throws IllegalAccessException {
        // Arrange: Setup search fields with null values
        Employee searchFields = Employee.builder().name(null).email(null).phone(null).build();

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<EmployeeDBO> criteriaQuery = mock(CriteriaQuery.class);
        Root<EmployeeDBO> root = mock(Root.class);
        TypedQuery<EmployeeDBO> typedQuery = mock(TypedQuery.class);
        CriteriaQuery<Long> countQuery = mock(CriteriaQuery.class);
        TypedQuery<Long> countTypedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(EmployeeDBO.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(EmployeeDBO.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(employeeDBO));

        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        when(countQuery.select(criteriaBuilder.count(countQuery.from(EmployeeDBO.class)))).thenReturn(countQuery);
        when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
        when(countTypedQuery.getSingleResult()).thenReturn(1L);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Employee> result = employeeRepository.findPaginated(searchFields, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testFindPaginated_withNonNullFieldsInSearchFields() throws IllegalAccessException {
        // Arrange: Setup search fields with non-null values
        Employee searchFields = Employee.builder().name("David").email("david@gmail.com").phone("12354353").build();

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<EmployeeDBO> criteriaQuery = mock(CriteriaQuery.class);
        Root<EmployeeDBO> root = mock(Root.class);
        TypedQuery<EmployeeDBO> typedQuery = mock(TypedQuery.class);
        CriteriaQuery<Long> countQuery = mock(CriteriaQuery.class);
        TypedQuery<Long> countTypedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(EmployeeDBO.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(EmployeeDBO.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(employeeDBO));

        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        when(countQuery.select(criteriaBuilder.count(countQuery.from(EmployeeDBO.class)))).thenReturn(countQuery);
        when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
        when(countTypedQuery.getSingleResult()).thenReturn(1L);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Employee> result = employeeRepository.findPaginated(searchFields, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testFindPaginated_withNullSort() throws IllegalAccessException {
        // Arrange
        Employee searchFields = Employee.builder().name("David").email("david@gmail.com").phone("12354353").build();

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<EmployeeDBO> criteriaQuery = mock(CriteriaQuery.class);
        Root<EmployeeDBO> root = mock(Root.class);
        TypedQuery<EmployeeDBO> typedQuery = mock(TypedQuery.class);
        CriteriaQuery<Long> countQuery = mock(CriteriaQuery.class);
        TypedQuery<Long> countTypedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(EmployeeDBO.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(EmployeeDBO.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(employeeDBO));

        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        when(countQuery.select(criteriaBuilder.count(countQuery.from(EmployeeDBO.class)))).thenReturn(countQuery);
        when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
        when(countTypedQuery.getSingleResult()).thenReturn(1L);

        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

        // Act
        Page<Employee> result = employeeRepository.findPaginated(searchFields, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testFindPaginated_withNonNullSort() throws IllegalAccessException {
        // Arrange
        Employee searchFields = Employee.builder().id("idtest").name("David").email("david@gmail.com").phone("12354353").build();

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<EmployeeDBO> criteriaQuery = mock(CriteriaQuery.class);
        Root<EmployeeDBO> root = mock(Root.class);
        TypedQuery<EmployeeDBO> typedQuery = mock(TypedQuery.class);
        CriteriaQuery<Long> countQuery = mock(CriteriaQuery.class);
        TypedQuery<Long> countTypedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(EmployeeDBO.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(EmployeeDBO.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(employeeDBO));

        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        when(countQuery.select(criteriaBuilder.count(countQuery.from(EmployeeDBO.class)))).thenReturn(countQuery);
        when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
        when(countTypedQuery.getSingleResult()).thenReturn(1L);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));

        // Act
        Page<Employee> result = employeeRepository.findPaginated(searchFields, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }
    @Test
    public void testFindPaginated_withDescendingOrderSorting() throws IllegalAccessException {
        // Arrange: Setup search fields
        Employee searchFields = Employee.builder().name("David").email("david@gmail.com").phone("12354353").build();

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<EmployeeDBO> criteriaQuery = mock(CriteriaQuery.class);
        Root<EmployeeDBO> root = mock(Root.class);
        TypedQuery<EmployeeDBO> typedQuery = mock(TypedQuery.class);
        CriteriaQuery<Long> countQuery = mock(CriteriaQuery.class);
        TypedQuery<Long> countTypedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(EmployeeDBO.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(EmployeeDBO.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(employeeDBO));

        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        when(countQuery.select(criteriaBuilder.count(countQuery.from(EmployeeDBO.class)))).thenReturn(countQuery);
        when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
        when(countTypedQuery.getSingleResult()).thenReturn(1L);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("name")));

        // Act
        Page<Employee> result = employeeRepository.findPaginated(searchFields, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }




}