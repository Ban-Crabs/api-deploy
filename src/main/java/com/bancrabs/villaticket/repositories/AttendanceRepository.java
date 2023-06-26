package com.bancrabs.villaticket.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID>{
    Page<Attendance> findByUserId(UUID userId, Pageable pageable);
    Page<Attendance> findByEventId(UUID eventId, Pageable pageable);
}
