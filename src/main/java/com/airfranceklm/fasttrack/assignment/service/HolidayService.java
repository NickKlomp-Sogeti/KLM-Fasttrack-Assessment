package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.dto.HolidayResponseDto;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
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
}
