package com.emp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emp.entities.LeaveRequest;
import com.emp.repository.LeaveRequestRepository;

import jakarta.transaction.Transactional;

@Service
public class LeaveRequestService {

	@Autowired
    private LeaveRequestRepository leaveRequestRepository;

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest getLeaveRequestById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leave) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);

        // Safely update all required fields
        leaveRequest.setStartDate(leave.getStartDate());
        leaveRequest.setEndDate(leave.getEndDate());
        leaveRequest.setReason(leave.getReason());
        leaveRequest.setStatus(leave.getStatus());

        if (leave.getEmployee() != null) {
            leaveRequest.setEmployee(leave.getEmployee());
        }

        return leaveRequestRepository.save(leaveRequest);
    }


    public void deleteLeaveRequest(Long id) {
        leaveRequestRepository.deleteById(id);
    }

    public List<LeaveRequest> getLeaveRequestsByEmployeeId(String employeeId) {
        return leaveRequestRepository.findByEmployee_Id(employeeId);
    }

	public List<LeaveRequest> getLeavesByUser(Long id) {
		// TODO Auto-generated method stub
		return leaveRequestRepository.findByEmployee_CreatedBy_Id(id);
	}
	
	public List<LeaveRequest> getRecentLeaveRequests(Long id) {
		System.out.println(id);
	    return leaveRequestRepository.findRecentLeaveRequestsByUser(id); // we'll define this in repository
	}

	@Transactional
	public void deleteLeaveRequestByEmployeeId(String id) {
		// TODO Auto-generated method stub
		leaveRequestRepository.deleteLeaveRequestByEmployee_Id(id);
	}


	
}
