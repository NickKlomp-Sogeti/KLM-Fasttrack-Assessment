package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.dto.HolidayResponseDto;
import com.airfranceklm.fasttrack.assignment.model.Employee;
import com.airfranceklm.fasttrack.assignment.model.Holiday;
import com.airfranceklm.fasttrack.assignment.model.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private HolidayService holidayService;

    @Test
    void getAllHolidays_mapsEntitiesToDtos() {
        // arrange
        Employee employee = new Employee("klm012345", "Nick");
        Holiday holiday = new Holiday(
                UUID.fromString("0f8fad5b-d9cb-469f-a165-70867728950e"),
                "Summerholidays",
                OffsetDateTime.parse("2022-08-02T08:00:00+00:00"),
                OffsetDateTime.parse("2022-08-16T08:00:00+00:00"),
                HolidayStatus.SCHEDULED,
                employee
        );

        when(holidayRepository.findAll()).thenReturn(List.of(holiday));

        // act
        List<HolidayResponseDto> result = holidayService.getAllHolidays();

        // assert
        assertEquals(1, result.size());
        HolidayResponseDto dto = result.get(0);

        assertEquals("0f8fad5b-d9cb-469f-a165-70867728950e", dto.holidayId());
        assertEquals("Summerholidays", dto.holidayLabel());
        assertEquals("klm012345", dto.employeeId());
        assertEquals(OffsetDateTime.parse("2022-08-02T08:00Z"), dto.startOfHoliday());
        assertEquals(OffsetDateTime.parse("2022-08-16T08:00Z"), dto.endOfHoliday());
        assertEquals(HolidayStatus.SCHEDULED, dto.status());

        verify(holidayRepository).findAll();
        verifyNoMoreInteractions(holidayRepository);
    }

    @Test
    void getHolidaysByEmployeeId_filtersInRepositoryAndMapsToDtos() {
        // arrange
        String employeeId = "klm012345";
        Employee employee = new Employee(employeeId, "Nick");

        Holiday holiday = new Holiday(
                UUID.fromString("11111111-2222-3333-4444-555555555555"),
                "Family Visit",
                null,
                null,
                HolidayStatus.REQUESTED,
                employee
        );

        when(holidayRepository.findByEmployeeEmployeeId(employeeId)).thenReturn(List.of(holiday));

        // act
        List<HolidayResponseDto> result = holidayService.getHolidaysByEmployeeId(employeeId);

        // assert
        assertEquals(1, result.size());
        assertEquals(employeeId, result.get(0).employeeId());
        assertEquals(HolidayStatus.REQUESTED, result.get(0).status());

        verify(holidayRepository).findByEmployeeEmployeeId(employeeId);
        verifyNoMoreInteractions(holidayRepository);
    }
}
