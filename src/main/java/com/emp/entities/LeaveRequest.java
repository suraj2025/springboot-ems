package com.emp.entities;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "leave_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
    @Id
    private String id;

    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status; // "Pending", "Approved", "Rejected"

    @DBRef
    private Employee employee;
}
