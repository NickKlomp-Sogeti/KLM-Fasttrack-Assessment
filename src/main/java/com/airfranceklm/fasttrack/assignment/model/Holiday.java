package com.airfranceklm.fasttrack.assignment.model;

import com.airfranceklm.fasttrack.assignment.model.enums.HolidayStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private UUID holidayId;

    @Column(nullable = false)
    private String holidayLabel;

    private OffsetDateTime startOfHoliday;

    private OffsetDateTime endOfHoliday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HolidayStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_employeeId", nullable = false)
    private Employee employee;
}
