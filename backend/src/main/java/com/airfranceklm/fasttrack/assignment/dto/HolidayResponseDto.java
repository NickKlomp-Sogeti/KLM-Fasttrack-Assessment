package com.airfranceklm.fasttrack.assignment.dto;

import com.airfranceklm.fasttrack.assignment.model.Holiday;
import com.airfranceklm.fasttrack.assignment.model.enums.HolidayStatus;

import java.time.OffsetDateTime;

public record HolidayResponseDto(
         String holidayId,
         String holidayLabel,
         String employeeId,
         OffsetDateTime startOfHoliday,
         OffsetDateTime endOfHoliday,
         HolidayStatus status
) {
    public static HolidayResponseDto from(Holiday h) {
        return new HolidayResponseDto(
                h.getHolidayId().toString(),
                h.getHolidayLabel(),
                h.getEmployee().getEmployeeId(),
                h.getStartOfHoliday() != null ? h.getStartOfHoliday() : null,
                h.getEndOfHoliday() != null ? h.getEndOfHoliday() : null,
                h.getStatus()
        );
    }
}
