package com.template.spring.core.infrastructure.persistence.repository;


import com.template.spring.core.application.exception.UnknownEntityException;
import com.template.spring.core.application.mapper.EmployeeMapper;
import com.template.spring.core.domain.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Stream;

import static com.template.spring.utils.TestParametersProvider.*;
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

    public static Stream<Arguments> provideTestCases() {

        return Stream.of(
                Arguments.of(EMPLOYEE1, EMPLOYEE_DBO1, "41fbfe88-ea44-4606-a565-858c6e3b2e9c"),
                Arguments.of(EMPLOYEE2, EMPLOYEE_DBO2, "92124e84-sa14-4n34-a452-858346457457")
        );
    }

    public static Stream<Arguments> provideFindPaginatedTestCases() {

        Pageable pageable10 = PageRequest.of(0, 10);
        Pageable pageableUnsorted = PageRequest.of(0, 10, Sort.unsorted());
        Pageable pageableAsc = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
        Pageable pageableDes = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("name")));

        return Stream.of(
                Arguments.of(EMPLOYEE1, pageable10, null, EMPLOYEE_DBO1),
                Arguments.of(EMPLOYEE_NULL, pageable10, EMPLOYEE_DBO1),
                Arguments.of(EMPLOYEE1, pageableUnsorted, EMPLOYEE_DBO1),
                Arguments.of(EMPLOYEE1, pageableAsc, EMPLOYEE_DBO1),
                Arguments.of(EMPLOYEE1, pageableDes, EMPLOYEE_DBO1),
                Arguments.of(EMPLOYEE2, pageable10, null, EMPLOYEE_DBO2),
                Arguments.of(EMPLOYEE_NULL, pageable10, EMPLOYEE_DBO2),
                Arguments.of(EMPLOYEE2, pageableUnsorted, EMPLOYEE_DBO2),
                Arguments.of(EMPLOYEE2, pageableAsc, EMPLOYEE_DBO2),
                Arguments.of(EMPLOYEE2, pageableDes, EMPLOYEE_DBO2),
                Arguments.of(null, pageableDes, EMPLOYEE_DBO2)
        );
    }

    @BeforeEach
    public void setUp() {
        employeeRepository = new EmployeeRepository(repository, mapper, entityManager);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testSave(Employee employee, EmployeeDBO employeeDBO) {
        when(mapper.EntityToDBO(employee)).thenReturn(employeeDBO);
        when(repository.save(employeeDBO)).thenReturn(employeeDBO);
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        Employee savedEmployee = employeeRepository.save(employee);

        assertNotNull(savedEmployee);
        assertEquals(employee.getName(), savedEmployee.getName());
        verify(repository).save(employeeDBO);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testGetById(Employee employee, EmployeeDBO employeeDBO, String id) throws UnknownEntityException {
        when(repository.findById(id)).thenReturn(Optional.of(employeeDBO));
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        Employee foundEmployee = employeeRepository.getById(id);

        assertNotNull(foundEmployee);
        assertEquals(employee.getName(), foundEmployee.getName());
        verify(repository).findById(id);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testGetByIdThrowsUnknownEntityException(Employee employee, EmployeeDBO employeeDBO, String id) {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UnknownEntityException.class, () -> employeeRepository.getById(id));
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testUpdate(Employee employee, EmployeeDBO employeeDBO, String id) throws UnknownEntityException {
        when(repository.findById(id)).thenReturn(Optional.of(employeeDBO));
        doNothing().when(mapper).updateDBOFromEntity(employee, employeeDBO);
        when(repository.save(employeeDBO)).thenReturn(employeeDBO);
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        Employee updatedEmployee = employeeRepository.update(id, employee);

        assertNotNull(updatedEmployee);
        assertEquals(employee.getName(), updatedEmployee.getName());
        verify(repository).findById(id);
        verify(repository).save(employeeDBO);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testUpdateThrowsUnknownEntityException(Employee employee, EmployeeDBO employeeDBO, String id) {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UnknownEntityException.class, () -> employeeRepository.update(id, employee));
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testGetAll(Employee employee, EmployeeDBO employeeDBO, String id) {
        List<EmployeeDBO> employeeDBOs = Collections.singletonList(employeeDBO);
        when(repository.findAll()).thenReturn(employeeDBOs);
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        List<Employee> employees = employeeRepository.getAll();

        assertNotNull(employees);
        assertEquals(1, employees.size());
        verify(repository).findAll();
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testDelete(Employee employee, EmployeeDBO employeeDBO, String id) {
        doNothing().when(repository).deleteById(id);

        employeeRepository.delete(id);

        verify(repository).deleteById(id);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testPatch(Employee employee, EmployeeDBO employeeDBO, String id) throws UnknownEntityException {
        when(repository.findById(id)).thenReturn(Optional.of(employeeDBO));
        when(repository.save(employeeDBO)).thenReturn(employeeDBO);
        when(mapper.DBOToEntity(employeeDBO)).thenReturn(employee);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "John Doe Updated");

        Employee patchedEmployee = employeeRepository.patch(id, updates);

        assertNotNull(patchedEmployee);
        verify(repository).findById(id);
        verify(repository).save(employeeDBO);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testPatchThrowsUnknownEntityException(Employee employee, EmployeeDBO employeeDBO, String id) {
        when(repository.findById(id)).thenReturn(Optional.empty());

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "John Doe Updated");

        assertThrows(UnknownEntityException.class, () -> employeeRepository.patch(id, updates));
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testPatchThrowsIllegalArgumentException(Employee employee, EmployeeDBO employeeDBO, String id) {
        when(repository.findById(id)).thenReturn(Optional.of(employeeDBO));

        Map<String, Object> updates = new HashMap<>();
        updates.put("id", "newId");

        assertThrows(IllegalArgumentException.class, () -> employeeRepository.patch(id, updates));
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testPatchThrowsIllegalArgumentExceptionOnFieldSetFailure(Employee employee, EmployeeDBO employeeDBO, String id) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nonExistentField", "newValue");

        when(repository.findById(id)).thenReturn(Optional.of(employeeDBO));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> employeeRepository.patch(id, updates));

        assertTrue(exception.getMessage().contains("Failed to set field: nonExistentField"));

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);
    }

    @ParameterizedTest
    @SuppressWarnings("unchecked")
    @MethodSource("provideFindPaginatedTestCases")
    public void testFindPaginated(Employee searchFields, Pageable pageable, EmployeeDBO employeeDBO) throws IllegalAccessException {

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

        Page<Employee> result = employeeRepository.findPaginated(searchFields, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }
}