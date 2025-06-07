package com.emp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
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

    @ManyToOne
    private User createdBy; // Admin who added the employee
}
