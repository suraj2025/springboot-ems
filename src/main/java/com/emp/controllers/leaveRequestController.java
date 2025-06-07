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
public class leaveRequestController {

	 @Autowired
	    private LeaveRequestService leaveRequestService;
	 @Autowired
     private JwtUtil jwtUtil;
	 @Autowired
	 UserRepository userRepository;
	 @Autowired
	 EmployeeRepository employeeRepository;
	 
	 @GetMapping("/my-leaves")
	 public ResponseEntity<List<LeaveRequest>> getMyLeaves(HttpServletRequest request) {
	     String token = request.getHeader("Authorization").substring(7);
	     String username = jwtUtil.extractUsername(token);
	     User user = (User) userRepository.findByUsername(username);

	     if (user == null) {
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	     }

	     List<LeaveRequest> leaves = leaveRequestService.getLeavesByUser(user.getId());
	     return ResponseEntity.ok(leaves);
	 }


	  

	 

	 @PostMapping
	 public ResponseEntity<?> createLeaveRequest(@RequestBody LeaveRequest leaveRequest, HttpServletRequest request) {
	     String authHeader = request.getHeader("Authorization");
	     if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
	     }

	     String token = authHeader.substring(7);
	     String username = jwtUtil.extractUsername(token);

	     User user = (User) userRepository.findByUsername(username);
	     if (user == null) {
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
	     }

	     // ✅ Correct: Use the employee ID sent from frontend
	     String empId = leaveRequest.getEmployee().getId();
	     Employee employee = employeeRepository.findById(empId)
	         .orElse(null);

	     if (employee == null || !employee.getCreatedBy().equals(user)) {
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid employee ID or unauthorized access");
	     }

	     leaveRequest.setEmployee(employee);

	     LeaveRequest savedLeave = leaveRequestService.createLeaveRequest(leaveRequest);
	     return ResponseEntity.ok(savedLeave);
	 }



	    @PutMapping("/{id}")
	    public LeaveRequest updateLeaveRequest(@PathVariable Long id, @RequestBody LeaveRequest leave) {
	    	
	        return leaveRequestService.updateLeaveRequest(id,leave);
	    }

	    @DeleteMapping("/{id}")
	    public void deleteLeaveRequest(@PathVariable Long id) {
	        leaveRequestService.deleteLeaveRequest(id);
	    }

	    @GetMapping("/employee/{employeeId}")
	    public List<LeaveRequest> getLeaveRequestsByEmployeeId(@PathVariable String employeeId) {
	        return leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
	    }
	    
	    @GetMapping("/recent")
	    public ResponseEntity<List<LeaveRequest>> getRecentLeaveRequests(HttpServletRequest request) {
	    	String token = request.getHeader("Authorization").substring(7);
		     String username = jwtUtil.extractUsername(token);
		     User user = (User) userRepository.findByUsername(username);

		     if (user == null) {
		         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		     }
	        List<LeaveRequest> recentLeaves = leaveRequestService.getRecentLeaveRequests(user.getId());
	        return ResponseEntity.ok(recentLeaves);
	    }

}
