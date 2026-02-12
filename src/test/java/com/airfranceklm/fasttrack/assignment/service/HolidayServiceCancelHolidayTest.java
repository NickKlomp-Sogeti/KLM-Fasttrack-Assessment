package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.exception.HolidayNotFoundException;
import com.airfranceklm.fasttrack.assignment.exception.HolidayValidationException;
import com.airfranceklm.fasttrack.assignment.model.Holiday;
import com.airfranceklm.fasttrack.assignment.model.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.repository.EmployeeRepository;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceCancelHolidayTest {

    @Mock
    HolidayRepository holidayRepository;

    @Mock
    EmployeeRepository employeeRepository; // HolidayService ctor heeft deze nodig

    @InjectMocks
    HolidayService holidayService;

    @Test
    void cancelHoliday_whenHolidayNotFound_throwsNotFound() {
        UUID id = UUID.randomUUID();
        when(holidayRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(HolidayNotFoundException.class, () -> holidayService.cancelHoliday(id));

        verify(holidayRepository).findById(id);
        verify(holidayRepository, never()).delete(any());
    }

    @Test
    void cancelHoliday_whenStatusNotScheduled_throwsValidation() {
        UUID id = UUID.randomUUID();

        Holiday h = new Holiday();
        h.setHolidayId(id);
        h.setStatus(HolidayStatus.REQUESTED); // not SCHEDULED
        h.setStartOfHoliday(OffsetDateTime.of(2099, 3, 10, 8, 0, 0, 0, ZoneOffset.UTC));

        when(holidayRepository.findById(id)).thenReturn(Optional.of(h));

        assertThrows(HolidayValidationException.class, () -> holidayService.cancelHoliday(id));

        verify(holidayRepository, never()).delete(any());
    }

    @Test
    void cancelHoliday_whenLessThan5WorkingDaysBeforeStart_throwsValidation() {
        UUID id = UUID.randomUUID();

        Holiday h = new Holiday();
        h.setHolidayId(id);
        h.setStatus(HolidayStatus.SCHEDULED);

        // Start date: morgen -> zeker < 5 werkdagen
        OffsetDateTime startTomorrow = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1);
        h.setStartOfHoliday(startTomorrow);

        when(holidayRepository.findById(id)).thenReturn(Optional.of(h));

        assertThrows(HolidayValidationException.class, () -> holidayService.cancelHoliday(id));

        verify(holidayRepository, never()).delete(any());
    }

    @Test
    void cancelHoliday_whenValidScheduledAndEnoughTime_deletesHoliday() {
        UUID id = UUID.randomUUID();

        Holiday h = new Holiday();
        h.setHolidayId(id);
        h.setStatus(HolidayStatus.SCHEDULED);

        // Ver genoeg in de toekomst -> zeker >= 5 werkdagen
        h.setStartOfHoliday(OffsetDateTime.of(2099, 3, 20, 8, 0, 0, 0, ZoneOffset.UTC));

        when(holidayRepository.findById(id)).thenReturn(Optional.of(h));

        assertDoesNotThrow(() -> holidayService.cancelHoliday(id));

        verify(holidayRepository).delete(h);
    }
}
