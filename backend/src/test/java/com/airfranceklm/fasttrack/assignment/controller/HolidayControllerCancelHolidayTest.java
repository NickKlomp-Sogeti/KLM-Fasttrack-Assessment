package com.airfranceklm.fasttrack.assignment.controller;

import com.airfranceklm.fasttrack.assignment.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HolidayController.class)
class HolidayControllerCancelHolidayTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    HolidayService holidayService;

    @Test
    void cancelHoliday_returns204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/holidays/" + id))
                .andExpect(status().isNoContent());

        verify(holidayService).cancelHoliday(id);
        verifyNoMoreInteractions(holidayService);
    }
}
