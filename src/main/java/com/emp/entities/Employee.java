package com.emp.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    private String id;

    private String name;
    private String email;
    private String department;
    private Double salary;

    // Adjusted to MongoDB @DBRef instead of @ManyToOne
    // private User createdBy;
    private String createdBy; // Assuming the ID of the User who created this employee
}
