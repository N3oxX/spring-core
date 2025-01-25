package com.template.spring.core.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeJpaRepository extends JpaRepository<EmployeeDBO, String> {
    // Additional query methods if needed
}