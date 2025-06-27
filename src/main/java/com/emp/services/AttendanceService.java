package com.emp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emp.dto.AttendanceResponseDTO;
import com.emp.entities.Attendance;
import com.emp.entities.Employee;
import com.emp.repository.AttendanceRepository;
import com.emp.repository.EmployeeRepository;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;
	@Autowired
	private EmployeeRepository employeeRepo;

	public List<Attendance> getAllAttendances() {
		return attendanceRepository.findAll();
	}

	public Attendance getAttendanceById(String id) {
		return attendanceRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));
	}

	public Attendance createAttendance(Attendance attendance) {
		return attendanceRepository.save(attendance);
	}

	public Attendance updateAttendance(String id, Attendance updatedAttendance) {
		Attendance attendance = getAttendanceById(id);
		attendance.setDate(updatedAttendance.getDate());
		attendance.setStatus(updatedAttendance.getStatus());
		attendance.setEmployee(updatedAttendance.getEmployee());
		return attendanceRepository.save(attendance);
	}

	public void deleteAttendance(String id) {
		attendanceRepository.deleteById(id);
	}

	public List<Attendance> getAttendancesByEmployeeId(String employeeId) {
		return attendanceRepository.findByEmployeeId(employeeId);
	}

	public List<AttendanceResponseDTO> getAttendanceForUser(String userId) {
	    List<Employee> employees = employeeRepo.findByCreatedBy(userId);
	    List<String> empIds = employees.stream()
	                                   .map(Employee::getId)
	                                   .collect(Collectors.toList());

	    List<Attendance> attendanceList = attendanceRepository.findByEmployeeIds(empIds);

	    return attendanceList.stream()
	            .map(att -> {
	                Employee emp = att.getEmployee();
	                return new AttendanceResponseDTO(
	                		att.getId(),
	                        att.getDate(),
	                        att.getStatus(),
	                        emp.getId(),
	                        emp.getName(),
	                        emp.getDepartment()
	                );
	            })
	            .collect(Collectors.toList());
	}


	public void deleteAttendanceByEmployeeId(String employeeId) {
		attendanceRepository.deleteByEmployeeId(employeeId);
	}
}
