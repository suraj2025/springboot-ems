package com.emp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emp.entities.Employee;
import com.emp.entities.User;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

	List<Employee> findByCreatedBy_Id(Long userId);

	List<Employee> findByCreatedBy(User user);


}
