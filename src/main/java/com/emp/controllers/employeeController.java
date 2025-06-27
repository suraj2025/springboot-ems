package com.emp.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.entities.Employee;
import com.emp.entities.User;
import com.emp.repository.UserRepository;
import com.emp.security.JwtUtil;
import com.emp.services.AttendanceService;
import com.emp.services.EmployeeService;
import com.emp.services.LeaveRequestService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired private AttendanceService attendanceService;
    @Autowired private LeaveRequestService leaveService;
    @Autowired private EmployeeService employeeService;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createEmployee(@RequestBody Employee employee, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }

        employee.setCreatedBy(userId);
        Employee saved = employeeService.createEmployee(employee);

        return ResponseEntity.ok(Map.of("message", "Employee created successfully", "employeeId", saved.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable String id, @RequestBody Employee employee, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }

        employee.setCreatedBy(userId);
        Employee updated = employeeService.updateEmployee(id, employee);

        return ResponseEntity.ok(Map.of("message", "Employee updated successfully", "employeeId", updated.getId()));
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {
        attendanceService.deleteAttendanceByEmployeeId(id);
        leaveService.deleteLeaveRequestByEmployeeId(id);
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/my-employees")
    public ResponseEntity<List<Employee>> getMyEmployees(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(employeeService.getEmployeesByUserId(userId));
    }

    private String extractUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) return null;

        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username);
        return user != null ? user.getId() : null;
    }
}
