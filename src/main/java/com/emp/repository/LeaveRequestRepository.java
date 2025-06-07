package com.emp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emp.entities.LeaveRequest;

import jakarta.transaction.Transactional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>{

	List<LeaveRequest> findByEmployee_Id(String employeeId);

	List<LeaveRequest> findByEmployee_CreatedBy_Id(Long id);
	@Query(
			  value = "SELECT lr.* FROM leave_request lr " +
			          "JOIN employee e ON lr.employee_id = e.id " +
			          "WHERE e.created_by_id = :id " +
			          "ORDER BY lr.id DESC LIMIT 5",
			  nativeQuery = true
			)
			List<LeaveRequest> findRecentLeaveRequestsByUser(@Param("id") Long createdById);


	void deleteLeaveRequestByEmployee_Id(@Param("employeeId") String employeeId);







}
