package com.emp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emp.entities.Attendance;

import jakarta.transaction.Transactional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

	List<Attendance> findByEmployee_Id(String employeeId);

	List<Attendance> findByEmployee_CreatedBy_Id(Long id);

//	@Transactional
//    @Modifying
//    @Query("DELETE FROM Attendance a WHERE a.employee_id = :employeeId")
    void deleteAttendanceByEmployee_Id(@Param("employeeId") String employeeId);

}
