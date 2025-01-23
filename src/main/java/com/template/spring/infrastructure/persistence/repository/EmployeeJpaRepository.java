package com.template.spring.infrastructure.persistence.repository;

import com.template.spring.infrastructure.persistence.dbo.EmployeeDBO;
import org.springframework.data.jpa.repository.JpaRepository;



public interface EmployeeJpaRepository extends JpaRepository<EmployeeDBO, String> {
    // Additional query methods if needed
}