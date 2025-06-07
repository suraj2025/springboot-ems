package com.emp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emp.entities.Employee;
import com.emp.entities.User;
import com.emp.repository.EmployeeRepository;

@Service
public class EmployeeService {
 
	@Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(String id, Employee updatedEmployee) {
    	System.out.println("Employee Service Called");
        Employee employee = getEmployeeById(id);
        employee.setName(updatedEmployee.getName());
        employee.setDepartment(updatedEmployee.getDepartment());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setSalary(updatedEmployee.getSalary());
        
        // Update other fields accordingly
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> getEmployeesByUserId(Long userId) {
        return employeeRepository.findByCreatedBy_Id(userId);
    }

	public List<Employee> getEmployeesByUser(Long id) {
		
		    return employeeRepository.findByCreatedBy_Id(id);
		

	}
	
	
}
