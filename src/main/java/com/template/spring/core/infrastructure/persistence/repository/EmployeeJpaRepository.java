package com.template.spring.core.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface EmployeeJpaRepository extends JpaRepository<EmployeeDBO, String>, JpaSpecificationExecutor<EmployeeDBO> {
    // Additional query methods if needed
}