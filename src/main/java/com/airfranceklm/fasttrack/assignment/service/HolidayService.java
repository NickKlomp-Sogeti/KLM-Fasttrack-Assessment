package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.dto.HolidayCreateRequestDto;
import com.airfranceklm.fasttrack.assignment.dto.HolidayResponseDto;
import com.airfranceklm.fasttrack.assignment.exception.EmployeeNotFoundException;
import com.airfranceklm.fasttrack.assignment.exception.HolidayNotFoundException;
import com.airfranceklm.fasttrack.assignment.exception.HolidayValidationException;
import com.airfranceklm.fasttrack.assignment.model.Employee;
import com.airfranceklm.fasttrack.assignment.model.Holiday;
import com.airfranceklm.fasttrack.assignment.model.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.repository.EmployeeRepository;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class HolidayService {

    private static final int MIN_DIFFERENT_WORKING_DAYS = 3;
    private static final int MIN_BOOKING_TIME = 5;

    private final HolidayRepository holidayRepository;
    private final EmployeeRepository employeeRepository;

    public HolidayService(HolidayRepository holidayRepository, EmployeeRepository employeeRepository) {
        this.holidayRepository = holidayRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<HolidayResponseDto> getAllHolidays() {
        return holidayRepository.findAll()
                .stream()
                .map(HolidayResponseDto::from)
                .toList();
    }

    public List<HolidayResponseDto> getHolidaysByEmployeeId(String employeeId) {
        return holidayRepository.findByEmployeeEmployeeId(employeeId)
                .stream()
                .map(HolidayResponseDto::from)
                .toList();
    }

    // Creates a new holiday based on the provided DTO. Validates the input and business rules before saving.
    public HolidayResponseDto createHoliday(HolidayCreateRequestDto dto) {
        HolidayStatus status;
        OffsetDateTime start = dto.startOfHoliday();
        OffsetDateTime end = dto.endOfHoliday();

        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee with ID " + dto.employeeId() + " not found"
                        ));

        if (dto.holidayLabel() == null || dto.holidayLabel().isBlank()) {
            throw new HolidayValidationException("holidayLabel is required");
        }

        if (start == null && end == null) {
            status = HolidayStatus.DRAFT;
        } else if (start != null && end != null) {
            status = HolidayStatus.REQUESTED;

            validateStartBeforeEnd(start, end);
            validateStartNotInPast(start);
            validatePlannedAtLeastWorkingDaysAhead(start);

            validateNoOverlapGlobal(start, end);

            validateGapForEmployee(employee.getEmployeeId(), start, end);
        } else {
            throw new HolidayValidationException("startOfHoliday and endOfHoliday must both be provided or both be null");
        }

        Holiday holiday = new Holiday();
        holiday.setHolidayLabel(dto.holidayLabel());
        holiday.setEmployee(employee);
        holiday.setStartOfHoliday(start);
        holiday.setEndOfHoliday(end);
        holiday.setStatus(status);

        Holiday saved = holidayRepository.save(holiday);
        return HolidayResponseDto.from(saved);
    }

    // Cancels a holiday by its ID. Only holidays with status SCHEDULED can be canceled, and there must be at least 5 working days between the cancellation date and the start of the holiday.
    public void cancelHoliday(UUID holidayId) {
        Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new HolidayNotFoundException("Holiday with ID " + holidayId + " not found"));

        if (holiday.getStatus() != HolidayStatus.SCHEDULED) {
            throw new HolidayValidationException("Only SCHEDULED holidays can be cancelled");
        }

        if (holiday.getStartOfHoliday() == null) {
            throw new HolidayValidationException("Cannot cancel holiday without a start date");
        }

        validateCancellationAtLeastWorkingDaysAhead(holiday.getStartOfHoliday(), MIN_BOOKING_TIME);

        holidayRepository.delete(holiday);
    }

    // Validates that the start date is before the end date
    private void validateStartBeforeEnd(OffsetDateTime start, OffsetDateTime end) {
        if (!start.isBefore(end)) {
            throw new HolidayValidationException("startOfHoliday must be before endOfHoliday");
        }
    }

    // Validates that the start date is not in the past (compared to current time in UTC)
    private void validateStartNotInPast(OffsetDateTime start) {
        OffsetDateTime nowUtc = OffsetDateTime.now(ZoneOffset.UTC);
        if (start.isBefore(nowUtc)) {
            throw new HolidayValidationException("startOfHoliday must not be in the past");
        }
    }

    // Validates that the holiday is planned at least MIN_BOOKING_TIME working days ahead of the start date
    private void validatePlannedAtLeastWorkingDaysAhead(OffsetDateTime start) {
        LocalDate todayUtc = OffsetDateTime.now(ZoneOffset.UTC).toLocalDate();
        LocalDate startDate = start.toLocalDate();

        long workingDays = countWorkingDaysExclusive(todayUtc, startDate);
        if (workingDays < HolidayService.MIN_BOOKING_TIME) {
            throw new HolidayValidationException("Holiday must be planned at least " + HolidayService.MIN_BOOKING_TIME + " working days before startOfHoliday");
        }
    }

    // Checks if the new holiday overlaps with any existing holiday (of any employee)
    private void validateNoOverlapGlobal(OffsetDateTime start, OffsetDateTime end) {
        boolean overlaps = holidayRepository.existsOverlappingHoliday(start, end);
        if (overlaps) {
            throw new HolidayValidationException("Holiday overlaps with an existing holiday");
        }
    }

    // Validates that there is a gap of at least MIN_DIFFERENT_WORKING_DAYS working days between the new holiday and any existing holiday of the same employee
    private void validateGapForEmployee(String employeeId, OffsetDateTime newStart, OffsetDateTime newEnd) {
        List<Holiday> existing = holidayRepository
                .findByEmployeeEmployeeIdAndStartOfHolidayIsNotNullAndEndOfHolidayIsNotNull(employeeId);

        LocalDate newStartDate = newStart.toLocalDate();
        LocalDate newEndDate = newEnd.toLocalDate();

        for (Holiday h : existing) {
            LocalDate existingStart = h.getStartOfHoliday().toLocalDate();
            LocalDate existingEnd = h.getEndOfHoliday().toLocalDate();

            long gap = -1;

            // If the new holiday is completely before the existing one, count the gap from new end to existing start
            if (!newEndDate.isAfter(existingStart)) {
                gap = countWorkingDaysExclusive(newEndDate, existingStart);
            }
            // If the new holiday is completely after the existing one, count the gap from existing end to new start
            else if (!existingEnd.isAfter(newStartDate)) {
                gap = countWorkingDaysExclusive(existingEnd, newStartDate);
            }

            // If the holidays overlap or are adjacent, gap will be 0 or negative, which is invalid
            if (gap >= 0 && gap < MIN_DIFFERENT_WORKING_DAYS) {
                throw new HolidayValidationException(
                        "There must be a gap of at least " + MIN_DIFFERENT_WORKING_DAYS + " working days between holidays"
                );
            }
        }
    }

    // Counts the number of working days between two dates. Counting starts form tomorrow
    private long countWorkingDaysExclusive(LocalDate startDate, LocalDate endDate) {
        if (!startDate.isBefore(endDate)) {
            return 0;
        }

        long count = 0;
        LocalDate d = startDate.plusDays(1);
        while (d.isBefore(endDate)) {
            if (isWorkingDay(d)) {
                count++;
            }
            d = d.plusDays(1);
        }
        return count;
    }

    // Checks if a given date is a working day (Monday to Friday)
    private boolean isWorkingDay(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        return dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY;
    }

    // Validates that the holiday can only be canceled if there are at least minWorkingDays working days between the cancellation date and the start of the holiday
    private void validateCancellationAtLeastWorkingDaysAhead(OffsetDateTime start, int minWorkingDays) {
        LocalDate todayUtc = OffsetDateTime.now(ZoneOffset.UTC).toLocalDate();
        LocalDate startDate = start.toLocalDate();

        long workingDays = countWorkingDaysExclusive(todayUtc, startDate);
        if (workingDays < minWorkingDays) {
            throw new HolidayValidationException(
                    "Holiday must be cancelled at least " + minWorkingDays + " working days before startOfHoliday"
            );
        }
    }
}
