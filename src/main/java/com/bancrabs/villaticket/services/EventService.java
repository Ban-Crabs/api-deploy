package com.bancrabs.villaticket.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.bancrabs.villaticket.models.dtos.save.SaveEventDTO;
import com.bancrabs.villaticket.models.entities.Event;

public interface EventService {
    Event saveEvent(SaveEventDTO data) throws Exception;
    Boolean updateEvent(UUID eventId, SaveEventDTO data) throws Exception;
    Boolean deleteEvent(UUID eventId) throws Exception;
    Boolean toggleVisibility(UUID eventId) throws Exception;

    Event findById(UUID id);
    Page<Event> findAll(int page, int size);
    Page<Event> findAllUpcomingEvents(int page, int size);
    Page<Event> findAllPastEvents(int page, int size);
    Page<Event> findAllEventsByType(String typeID, int page, int size);
    Page<Event> findAllEventsByLocation(String locationID, int page, int size);
    Page<Event> findAllVisibleEvents(int page, int size);
    Page<Event> findAllInvisibleEvents(int page, int size);
}
