package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.dto.HolidayCreateRequestDto;
import com.airfranceklm.fasttrack.assignment.dto.HolidayResponseDto;
import com.airfranceklm.fasttrack.assignment.exception.EmployeeNotFoundException;
import com.airfranceklm.fasttrack.assignment.exception.HolidayValidationException;
import com.airfranceklm.fasttrack.assignment.model.Employee;
import com.airfranceklm.fasttrack.assignment.model.Holiday;
import com.airfranceklm.fasttrack.assignment.model.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.repository.EmployeeRepository;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceCreateHolidayTest {

    @Mock HolidayRepository holidayRepository;
    @Mock EmployeeRepository employeeRepository;

    @InjectMocks HolidayService holidayService;

    private static final String EMPLOYEE_ID = "klm012345";

    @Test
    void createHoliday_whenEmployeeDoesNotExist_throwsEmployeeNotFound() {
        HolidayCreateRequestDto dto = new HolidayCreateRequestDto(
                "Summerholidays",
                EMPLOYEE_ID,
                null,
                null
        );

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> holidayService.createHoliday(dto));

        verify(employeeRepository).findById(EMPLOYEE_ID);
        verifyNoInteractions(holidayRepository);
    }

    @Test
    void createHoliday_whenNoDates_setsStatusDraft_andSaves() {
        Employee employee = new Employee(EMPLOYEE_ID, "Nick");
        HolidayCreateRequestDto dto = new HolidayCreateRequestDto(
                "Draft holiday",
                EMPLOYEE_ID,
                null,
                null
        );

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));

        // saved entity simuleren
        Holiday saved = new Holiday();
        saved.setHolidayId(UUID.randomUUID());
        saved.setHolidayLabel("Draft holiday");
        saved.setEmployee(employee);
        saved.setStartOfHoliday(null);
        saved.setEndOfHoliday(null);
        saved.setStatus(HolidayStatus.DRAFT);

        when(holidayRepository.save(any(Holiday.class))).thenReturn(saved);

        HolidayResponseDto result = holidayService.createHoliday(dto);

        assertEquals("Draft holiday", result.holidayLabel());
        assertEquals(EMPLOYEE_ID, result.employeeId());
        assertNull(result.startOfHoliday());
        assertNull(result.endOfHoliday());
        assertEquals(HolidayStatus.DRAFT, result.status());

        // Belangrijk: bij DRAFT horen overlap/gap checks niet te lopen
        verify(holidayRepository, never()).existsOverlappingHoliday(any(), any());
        verify(holidayRepository, never()).findByEmployeeEmployeeIdAndStartOfHolidayIsNotNullAndEndOfHolidayIsNotNull(any());
    }

    @Test
    void createHoliday_whenOnlyStartProvided_throwsValidationException() {
        Employee employee = new Employee(EMPLOYEE_ID, "Nick");
        HolidayCreateRequestDto dto = new HolidayCreateRequestDto(
                "Invalid",
                EMPLOYEE_ID,
                OffsetDateTime.of(2099, 1, 10, 8, 0, 0, 0, ZoneOffset.UTC),
                null
        );

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));

        assertThrows(HolidayValidationException.class, () -> holidayService.createHoliday(dto));

        verify(holidayRepository, never()).save(any());
    }

    @Test
    void createHoliday_whenStartAfterEnd_throwsValidationException() {
        Employee employee = new Employee(EMPLOYEE_ID, "Nick");
        HolidayCreateRequestDto dto = new HolidayCreateRequestDto(
                "Invalid dates",
                EMPLOYEE_ID,
                OffsetDateTime.of(2099, 1, 20, 8, 0, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.of(2099, 1, 10, 8, 0, 0, 0, ZoneOffset.UTC)
        );

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));

        assertThrows(HolidayValidationException.class, () -> holidayService.createHoliday(dto));

        verify(holidayRepository, never()).save(any());
    }

    @Test
    void createHoliday_whenOverlapsGlobally_throwsValidationException() {
        Employee employee = new Employee(EMPLOYEE_ID, "Nick");
        OffsetDateTime start = OffsetDateTime.of(2099, 1, 10, 8, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime end = OffsetDateTime.of(2099, 1, 20, 8, 0, 0, 0, ZoneOffset.UTC);

        HolidayCreateRequestDto dto = new HolidayCreateRequestDto("Overlap", EMPLOYEE_ID, start, end);

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        when(holidayRepository.existsOverlappingHoliday(start, end)).thenReturn(true);

        assertThrows(HolidayValidationException.class, () -> holidayService.createHoliday(dto));

        verify(holidayRepository, never()).save(any());
    }

    @Test
    void createHoliday_whenGapLessThan3WorkingDaysForEmployee_throwsValidationException() {
        Employee employee = new Employee(EMPLOYEE_ID, "Nick");

        // new holiday: 2099-01-11 to 2099-01-12
        OffsetDateTime newStart = OffsetDateTime.of(2099, 1, 11, 8, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime newEnd = OffsetDateTime.of(2099, 1, 12, 8, 0, 0, 0, ZoneOffset.UTC);
        HolidayCreateRequestDto dto = new HolidayCreateRequestDto("Too close", EMPLOYEE_ID, newStart, newEnd);

        // existing holiday ends exactly at newStart date -> gap 0 (altijd < 3)
        Holiday existing = new Holiday();
        existing.setHolidayId(UUID.randomUUID());
        existing.setHolidayLabel("Existing");
        existing.setEmployee(employee);
        existing.setStartOfHoliday(OffsetDateTime.of(2099, 1, 5, 8, 0, 0, 0, ZoneOffset.UTC));
        existing.setEndOfHoliday(OffsetDateTime.of(2099, 1, 11, 8, 0, 0, 0, ZoneOffset.UTC));
        existing.setStatus(HolidayStatus.SCHEDULED);

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        when(holidayRepository.existsOverlappingHoliday(newStart, newEnd)).thenReturn(false);
        when(holidayRepository.findByEmployeeEmployeeIdAndStartOfHolidayIsNotNullAndEndOfHolidayIsNotNull(EMPLOYEE_ID))
                .thenReturn(List.of(existing));

        assertThrows(HolidayValidationException.class, () -> holidayService.createHoliday(dto));

        verify(holidayRepository, never()).save(any());
    }

    @Test
    void createHoliday_whenValidRequested_savesWithStatusRequested() {
        Employee employee = new Employee(EMPLOYEE_ID, "Nick");

        // Ver in de toekomst: voorkomt flakiness door "nu" checks
        OffsetDateTime start = OffsetDateTime.of(2099, 2, 10, 8, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime end = OffsetDateTime.of(2099, 2, 20, 8, 0, 0, 0, ZoneOffset.UTC);
        HolidayCreateRequestDto dto = new HolidayCreateRequestDto("Valid", EMPLOYEE_ID, start, end);

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        when(holidayRepository.existsOverlappingHoliday(start, end)).thenReturn(false);
        when(holidayRepository.findByEmployeeEmployeeIdAndStartOfHolidayIsNotNullAndEndOfHolidayIsNotNull(EMPLOYEE_ID))
                .thenReturn(List.of());

        // capture wat we opslaan
        ArgumentCaptor<Holiday> captor = ArgumentCaptor.forClass(Holiday.class);

        Holiday saved = new Holiday();
        saved.setHolidayId(UUID.randomUUID());
        saved.setHolidayLabel("Valid");
        saved.setEmployee(employee);
        saved.setStartOfHoliday(start);
        saved.setEndOfHoliday(end);
        saved.setStatus(HolidayStatus.REQUESTED);

        when(holidayRepository.save(any(Holiday.class))).thenReturn(saved);

        HolidayResponseDto result = holidayService.createHoliday(dto);

        verify(holidayRepository).save(captor.capture());
        Holiday toSave = captor.getValue();

        assertEquals("Valid", toSave.getHolidayLabel());
        assertEquals(employee, toSave.getEmployee());
        assertEquals(start, toSave.getStartOfHoliday());
        assertEquals(end, toSave.getEndOfHoliday());
        assertEquals(HolidayStatus.REQUESTED, toSave.getStatus());

        assertEquals(HolidayStatus.REQUESTED, result.status());
    }
}
