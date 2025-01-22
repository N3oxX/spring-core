package com.template.spring.infrastructure.persistence.mongo.repository;

import com.template.spring.infrastructure.persistence.mongo.dbo.EmployeeDBO;
import org.springframework.data.jpa.repository.JpaRepository;



public interface EmployeeJpaRepository extends JpaRepository<EmployeeDBO, String> {
    // Additional query methods if needed
}