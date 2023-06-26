package com.bancrabs.villaticket.repositories;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Event;

public interface EventRepository extends JpaRepository<Event, UUID>{
    Page<Event> findByIsVisibleIsTrue(Pageable pageable);
    Page<Event> findByIsVisibleIsFalse(Pageable pageable);
    Page<Event> findAllByOrderByDateAsc(Pageable pageable);
    Page<Event> findAllByOrderByDateDesc(Pageable pageable);
    Page<Event> findByEndTimeIsNotNull(Pageable pageable);
    Page<Event> findByEndTimeIsNull(Pageable pageable);
    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Event> findByTypeId(String typeID, Pageable pageable);
    Page<Event> findByLocationId(String locationID, Pageable pageable);
    Event findByTitleAndDateAndStartTime(String title, Date date, Timestamp startTime);
}
