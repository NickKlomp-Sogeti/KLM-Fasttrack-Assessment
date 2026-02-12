package com.airfranceklm.fasttrack.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.OffsetDateTime;

public record HolidayCreateRequestDto(
        @NotBlank(message = "holidayLabel is required")
        String holidayLabel,

        @NotBlank(message = "employeeId is required")
        @Pattern(regexp = "^klm[0-9]{6}$", message = "employeeId must match pattern klmXXXXXX")
        String employeeId,

        OffsetDateTime startOfHoliday,
        OffsetDateTime endOfHoliday
) { }
