package com.bancrabs.villaticket.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.bancrabs.villaticket.models.dtos.save.RecordAttendanceDTO;
import com.bancrabs.villaticket.models.entities.Attendance;

public interface AttendanceService {
    Boolean save(RecordAttendanceDTO data) throws Exception;

    Page<Attendance> findAll(int page, int size);
    Page<Attendance> findByUserId(UUID userId, int page, int size);
    Page<Attendance> findByEventId(UUID eventId, int page, int size);
}
