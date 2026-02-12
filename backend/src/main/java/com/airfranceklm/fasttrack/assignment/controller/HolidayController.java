package com.airfranceklm.fasttrack.assignment.controller;

import java.util.List;
import java.util.UUID;

import com.airfranceklm.fasttrack.assignment.dto.HolidayCreateRequestDto;
import com.airfranceklm.fasttrack.assignment.dto.HolidayResponseDto;
import com.airfranceklm.fasttrack.assignment.service.HolidayService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
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

    @PostMapping
    public ResponseEntity<HolidayResponseDto> createHoliday( @Valid
            @RequestBody HolidayCreateRequestDto holidayCreateRequestDto) {

        HolidayResponseDto created = holidayService.createHoliday(holidayCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @DeleteMapping("/{holidayId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelHoliday(@PathVariable UUID holidayId) {
        holidayService.cancelHoliday(holidayId);
    }
}
