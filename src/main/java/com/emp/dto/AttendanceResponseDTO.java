package com.emp.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class AttendanceResponseDTO {
	private String id;
    private LocalDate date;
    private String status;
    private String employeeId;
    private String name;
    private String department;

    // Constructor
    public AttendanceResponseDTO(String id,LocalDate localDate, String status, String employeeId, String name, String department) {
        this.id=id;
    	this.date = localDate;
        this.status = status;
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
    }

    // Getters and setters
}

