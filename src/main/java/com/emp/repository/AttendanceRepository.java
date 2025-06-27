package com.emp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.emp.entities.Attendance;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByEmployeeId(String employeeId);
    void deleteByEmployeeId(String employeeId);
	List<Attendance> findByEmployee_CreatedBy(String userId);
	@Query("{ 'employee.$id' : { $in: ?0 } }")
	List<Attendance> findByEmployeeIds(List<String> empIds);

}
