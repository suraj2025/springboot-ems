package com.emp.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeavesDTO {
	private String id;

    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
    private String employeeId;
    private String name;

}
