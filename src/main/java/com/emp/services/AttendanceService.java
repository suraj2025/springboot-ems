package com.emp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emp.entities.Attendance;
import com.emp.repository.AttendanceRepository;

import jakarta.transaction.Transactional;

@Service
public class AttendanceService {

	@Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));
    }

    public Attendance createAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public Attendance updateAttendance(Long id, Attendance updatedAttendance) {
        Attendance attendance = getAttendanceById(id);
        attendance.setDate(updatedAttendance.getDate());
        attendance.setStatus(updatedAttendance.getStatus());
        // Update other fields accordingly
        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    public List<Attendance> getAttendancesByEmployeeId(String employeeId) {
        return attendanceRepository.findByEmployee_Id(employeeId);
    }

	public List<Attendance> getAttendanceByUser(Long id) {
		// TODO Auto-generated method stub
		return attendanceRepository.findByEmployee_CreatedBy_Id(id);
	}
    
	@Transactional
	public void deleteAttendanceByEmployeeId(String employeeId) {
	    attendanceRepository.deleteAttendanceByEmployee_Id(employeeId);
	}

}
