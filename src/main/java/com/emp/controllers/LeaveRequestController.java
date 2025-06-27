package com.emp.controllers;

import java.util.List;

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

import com.emp.dto.AttendanceResponseDTO;
import com.emp.dto.LeavesDTO;
import com.emp.entities.Employee;
import com.emp.entities.LeaveRequest;
import com.emp.entities.User;
import com.emp.repository.EmployeeRepository;
import com.emp.repository.UserRepository;
import com.emp.security.JwtUtil;
import com.emp.services.LeaveRequestService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/leaveRequests")
public class LeaveRequestController {

    @Autowired private LeaveRequestService leaveRequestService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserRepository userRepository;
    @Autowired private EmployeeRepository employeeRepository;

    @GetMapping("/my-leaves")
    public ResponseEntity<List<LeavesDTO>> getMyLeaves(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        List<LeavesDTO> leaves = leaveRequestService.getAttendanceForUser(userId);
      return ResponseEntity.ok(leaves);
    }

    @PostMapping
    public ResponseEntity<?> createLeaveRequest(@RequestBody LeaveRequest leaveRequest, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        String empId = leaveRequest.getEmployee().getId();
        Employee employee = employeeRepository.findById(empId).orElse(null);

        if (employee == null || !employee.getCreatedBy().equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid employee ID or unauthorized access");
        }

        leaveRequest.setEmployee(employee);
        LeaveRequest savedLeave = leaveRequestService.createLeaveRequest(leaveRequest);
        return ResponseEntity.ok(savedLeave);
    }

    @PutMapping("/{id}")
    public LeaveRequest updateLeaveRequest(@PathVariable String id, @RequestBody LeaveRequest leave) {
        return leaveRequestService.updateLeaveRequest(id, leave);
    }

    @DeleteMapping("/{id}")
    public void deleteLeaveRequest(@PathVariable String id) {
        leaveRequestService.deleteLeaveRequest(id);
    }

    @GetMapping("/employee/{employeeId}")
    public List<LeaveRequest> getLeaveRequestsByEmployeeId(@PathVariable String employeeId) {
        return leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<LeavesDTO>> getRecentLeaveRequests(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(leaveRequestService.getRecentLeaveRequests(userId));
    }

    private String extractUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) return null;

        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username);
        return user != null ? user.getId() : null;
    }
}
