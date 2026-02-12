package com.airfranceklm.fasttrack.assignment.controller;

import com.airfranceklm.fasttrack.assignment.dto.HolidayResponseDto;
import com.airfranceklm.fasttrack.assignment.model.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.service.HolidayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HolidayController.class)
class HolidayControllerCreateHolidayTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean HolidayService holidayService;

    @Test
    void createHoliday_returns201AndBody() throws Exception {
        HolidayResponseDto response = new HolidayResponseDto(
                UUID.randomUUID().toString(),
                "Summerholidays",
                "klm012345",
                OffsetDateTime.of(2099, 2, 10, 8, 0, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.of(2099, 2, 20, 8, 0, 0, 0, ZoneOffset.UTC),
                HolidayStatus.REQUESTED
        );

        when(holidayService.createHoliday(any())).thenReturn(response);

        // Request body (let op: status zit niet in request)
        String json = """
            {
              "holidayLabel": "Summerholidays",
              "employeeId": "klm012345",
              "startOfHoliday": "2099-02-10T08:00:00Z",
              "endOfHoliday": "2099-02-20T08:00:00Z"
            }
        """;

        mockMvc.perform(post("/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.holidayLabel").value("Summerholidays"))
                .andExpect(jsonPath("$.employeeId").value("klm012345"))
                .andExpect(jsonPath("$.status").value("REQUESTED"));

        verify(holidayService).createHoliday(any());
        verifyNoMoreInteractions(holidayService);
    }

    @Test
    void createHoliday_whenDtoValidationFails_returns400() throws Exception {
        // Dit werkt alleen als je DTO annotations hebt (@NotBlank, @Pattern, etc.)
        String json = """
            {
              "holidayLabel": "",
              "employeeId": "wrong",
              "startOfHoliday": null,
              "endOfHoliday": null
            }
        """;

        mockMvc.perform(post("/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(holidayService);
    }
}
