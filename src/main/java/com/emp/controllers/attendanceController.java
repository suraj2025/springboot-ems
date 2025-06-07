package com.emp.controllers;

import java.net.http.HttpRequest;
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

import com.emp.entities.Attendance;
import com.emp.entities.User;
import com.emp.repository.UserRepository;
import com.emp.security.JwtUtil;
import com.emp.services.AttendanceService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/attendances")
public class attendanceController {

	 @Autowired
	    private AttendanceService attendanceService;
	 @Autowired
        private JwtUtil jwtUtil;
	 @Autowired
	 UserRepository userRepository;
	 @GetMapping("/my-attendance")
	    public ResponseEntity<?> getAttendanceForCurrentUser(HttpServletRequest request) {
	        String authHeader = request.getHeader("Authorization");
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of("message", "Unauthorized: Missing or invalid token"));
	        }

	        String token = authHeader.substring(7);
	        String username = jwtUtil.extractUsername(token);
	        User user = (User) userRepository.findByUsername(username);

	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of("message", "Unauthorized: User not found"));
	        }

	        // Fetch all attendance records linked to employees created by this user
	        List<Attendance> attendanceList = attendanceService.getAttendanceByUser(user.getId());

	        return ResponseEntity.ok(attendanceList);
	    }

	    @GetMapping("/{id}")
	    public Attendance getAttendanceById(@PathVariable Long id) {
	        return attendanceService.getAttendanceById(id);
	    }

	    @PostMapping
	    public Attendance createAttendance(@RequestBody Attendance attendance) {
	        return attendanceService.createAttendance(attendance);
	    }

	    @PutMapping("/{id}")
	    public Attendance updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
	        return attendanceService.updateAttendance(id, attendance);
	    }

	    @DeleteMapping("/{id}")
	    public void deleteAttendance(@PathVariable Long id) {
	    	System.out.println("Deleting...");
	        attendanceService.deleteAttendance(id);
	    }

	    @GetMapping("/employee/{employeeId}")
	    public List<Attendance> getAttendancesByEmployeeId(@PathVariable String employeeId) {
	        return attendanceService.getAttendancesByEmployeeId(employeeId);
	    }
}
