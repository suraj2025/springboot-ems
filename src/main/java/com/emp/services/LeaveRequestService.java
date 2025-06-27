package com.emp.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emp.dto.AttendanceResponseDTO;
import com.emp.dto.LeavesDTO;
import com.emp.entities.Attendance;
import com.emp.entities.Employee;
import com.emp.entities.LeaveRequest;
import com.emp.repository.EmployeeRepository;
import com.emp.repository.LeaveRequestRepository;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
	private EmployeeRepository employeeRepo;

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest getLeaveRequestById(String id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest updateLeaveRequest(String id, LeaveRequest leave) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);

        leaveRequest.setStartDate(leave.getStartDate());
        leaveRequest.setEndDate(leave.getEndDate());
        leaveRequest.setReason(leave.getReason());
        leaveRequest.setStatus(leave.getStatus());
        leaveRequest.setEmployee(leave.getEmployee());

        return leaveRequestRepository.save(leaveRequest);
    }

    public void deleteLeaveRequest(String id) {
        leaveRequestRepository.deleteById(id);
    }

    public List<LeaveRequest> getLeaveRequestsByEmployeeId(String employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    public List<LeaveRequest> getLeavesByUser(String createdByUserId) {
        return leaveRequestRepository.findByEmployee_CreatedBy(createdByUserId);
    }

    public List<LeavesDTO> getRecentLeaveRequests(String userId) {
    	List<Employee> employees = employeeRepo.findByCreatedBy(userId);
	    List<String> empIds = employees.stream()
	                                   .map(Employee::getId)
	                                   .collect(Collectors.toList());

	    List<LeaveRequest> leavesList = leaveRequestRepository.findByEmployeeIds(empIds);

	    return leavesList.stream()
        .sorted(Comparator.comparing(LeaveRequest::getStartDate).reversed())
        .limit(3)
        .map(att -> {
            Employee emp = att.getEmployee();
            return new LeavesDTO(
                    att.getId(),
                    att.getStartDate(),
                    att.getEndDate(),
                    att.getReason(),
                    att.getStatus(),
                    emp.getId(),
                    emp.getName()
            );
        })
        .collect(Collectors.toList());
    }

    public void deleteLeaveRequestByEmployeeId(String employeeId) {
        leaveRequestRepository.deleteByEmployeeId(employeeId);
    }

	public List<LeavesDTO> getAttendanceForUser(String userId) {
		List<Employee> employees = employeeRepo.findByCreatedBy(userId);
	    List<String> empIds = employees.stream()
	                                   .map(Employee::getId)
	                                   .collect(Collectors.toList());

	    List<LeaveRequest> leavesList = leaveRequestRepository.findByEmployeeIds(empIds);

	    return leavesList.stream()
	            .map(att -> {
	                Employee emp = att.getEmployee();
	                return new LeavesDTO(
	                		att.getId(),
	                        att.getStartDate(),
	                        att.getEndDate(),
	                        att.getReason(),
	                        att.getStatus(),
	                        emp.getId(),
	                        emp.getName()
	                );
	            })
	            .collect(Collectors.toList());
	}
}
