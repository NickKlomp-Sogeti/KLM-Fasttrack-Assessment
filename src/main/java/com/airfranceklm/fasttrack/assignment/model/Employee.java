package com.airfranceklm.fasttrack.assignment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {

    @Id
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^klm[0-9]{6}$", message = "Employee ID must match pattern klmXXXXXX")
    private String employeeId;

    @Column(nullable = false)
    private String name;
}
