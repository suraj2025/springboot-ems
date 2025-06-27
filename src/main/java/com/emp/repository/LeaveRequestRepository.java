package com.emp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.emp.entities.Attendance;
import com.emp.entities.LeaveRequest;

public interface LeaveRequestRepository extends MongoRepository<LeaveRequest, String> {
    List<LeaveRequest> findByEmployeeId(String employeeId);
    List<LeaveRequest> findByEmployee_CreatedBy(String createdBy); // If using @DBRef in Employee
    void deleteByEmployeeId(String employeeId);
    @Query("{ 'employee.$id' : { $in: ?0 } }")
	List<LeaveRequest> findByEmployeeIds(List<String> empIds);
}
