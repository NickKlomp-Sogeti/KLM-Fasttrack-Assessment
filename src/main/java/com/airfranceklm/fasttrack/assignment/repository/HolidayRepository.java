package com.airfranceklm.fasttrack.assignment.repository;

import com.airfranceklm.fasttrack.assignment.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, UUID> {
    List<Holiday> findByEmployeeEmployeeId(String employeeId);

    List<Holiday> findByEmployeeEmployeeIdAndStartOfHolidayIsNotNullAndEndOfHolidayIsNotNull(String employeeId);

    @Query("""
        select (count(h) > 0)
        from Holiday h
        where h.startOfHoliday is not null
          and h.endOfHoliday is not null
          and :start < h.endOfHoliday
          and :end > h.startOfHoliday
    """)
    boolean existsOverlappingHoliday(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);
}
