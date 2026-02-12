package com.airfranceklm.fasttrack.assignment.controller;

import com.airfranceklm.fasttrack.assignment.dto.HolidayResponseDto;
import com.airfranceklm.fasttrack.assignment.model.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HolidayController.class)
class HolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HolidayService holidayService;

    @Test
        void getHolidays_returnsOkAndJsonArray() throws Exception {
        when(holidayService.getAllHolidays()).thenReturn(List.of(
                new HolidayResponseDto(
                        "0f8fad5b-d9cb-469f-a165-70867728950e",
                        "Summerholidays",
                        "klm012345",
                        OffsetDateTime.parse("2022-08-02T08:00:00+00:00"),
                        OffsetDateTime.parse("2022-08-16T08:00:00+00:00"),
                        HolidayStatus.SCHEDULED
                )
        ));

        mockMvc.perform(get("/holidays"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].holidayId").value("0f8fad5b-d9cb-469f-a165-70867728950e"))
                .andExpect(jsonPath("$[0].employeeId").value("klm012345"))
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));

        verify(holidayService).getAllHolidays();
        verifyNoMoreInteractions(holidayService);
    }

    @Test
    void getHolidaysByEmployeeId_returnsOkAndCallsServiceWithPathVariable() throws Exception {
        when(holidayService.getHolidaysByEmployeeId("klm012345")).thenReturn(List.of(
                new HolidayResponseDto(
                        "11111111-2222-3333-4444-555555555555",
                        "Family Visit",
                        "klm012345",
                        null,
                        null,
                        HolidayStatus.REQUESTED
                )
        ));

        mockMvc.perform(get("/holidays/employee/klm012345"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].employeeId").value("klm012345"))
                .andExpect(jsonPath("$[0].status").value("REQUESTED"));

        verify(holidayService).getHolidaysByEmployeeId("klm012345");
        verifyNoMoreInteractions(holidayService);
    }
}
