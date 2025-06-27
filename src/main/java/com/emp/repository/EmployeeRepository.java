package com.emp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.emp.entities.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    List<Employee> findByCreatedBy(String userId); // userId is stored as String
}
