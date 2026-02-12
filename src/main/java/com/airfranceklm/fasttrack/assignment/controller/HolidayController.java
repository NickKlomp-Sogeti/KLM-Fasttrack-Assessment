package com.airfranceklm.fasttrack.assignment.controller;

import java.util.List;

import com.airfranceklm.fasttrack.assignment.dto.HolidayResponseDto;
import com.airfranceklm.fasttrack.assignment.service.HolidayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public List<HolidayResponseDto> getHolidays() {
        return holidayService.getAllHolidays();
    }

    @GetMapping("/employee/{employeeId}")
    public List<HolidayResponseDto> getHolidaysByEmployeeId(@PathVariable String employeeId) {
        return holidayService.getHolidaysByEmployeeId(employeeId);
    }
}
