package com.bancrabs.villaticket.services.implementations;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.SaveEventDTO;
import com.bancrabs.villaticket.models.entities.Category;
import com.bancrabs.villaticket.models.entities.Event;
import com.bancrabs.villaticket.models.entities.Location;
import com.bancrabs.villaticket.models.entities.Type;
import com.bancrabs.villaticket.repositories.EventRepository;
import com.bancrabs.villaticket.services.CategoryService;
import com.bancrabs.villaticket.services.EventService;
import com.bancrabs.villaticket.services.LocationService;
import com.bancrabs.villaticket.services.TypeService;

import jakarta.transaction.Transactional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TypeService typeService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Event saveEvent(SaveEventDTO data) throws Exception{
        try{
            Type type = typeService.findByNameOrId(data.getTypeId());
            if(type == null){
                throw new Exception("Type not found");
            }
            Location location = locationService.findByIdOrName(data.getLocationId());
            if(location == null){
                throw new Exception("Location not found");
            }
            Category category = categoryService.findById(data.getCategoryId());
            if(category == null){
                throw new Exception("Category not found");
            }
            Event toSave = eventRepository.findByTitleAndDateAndStartTime(data.getTitle(), data.getDate(), data.getStartTime());
            if(toSave == null){
                toSave = new Event(data.getTitle(), type, location, category, data.getDate(), data.getStartTime(), data.getEndTime(), data.getStatus(), data.getIsVisible());
                toSave = eventRepository.save(toSave);
                return toSave;
            }
            else{
                throw new Exception("Event already exists");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean updateEvent(UUID eventId, SaveEventDTO data) throws Exception {
        try{
            Type type = typeService.findByNameOrId(data.getTypeId());
            if(type == null){
                throw new Exception("Type not found");
            }
            Location location = locationService.findByIdOrName(data.getLocationId());
            if(location == null){
                throw new Exception("Location not found");
            }
            Event toUpdate = eventRepository.findById(eventId).orElse(null);
            if(toUpdate != null){
                toUpdate.setTitle(data.getTitle());
                toUpdate.setType(type);
                toUpdate.setLocation(location);
                toUpdate.setDate(data.getDate());
                toUpdate.setStartTime(data.getStartTime());
                toUpdate.setEndTime(data.getEndTime());
                toUpdate.setStatus(data.getStatus());
                toUpdate.setIsVisible(data.getIsVisible());
                eventRepository.save(toUpdate);
                return true;
            }
            else{
                throw new Exception("Event not found");
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Page<Event> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAll(pageable);
    }

    @Override
    public Page<Event> findAllUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByEndTimeIsNull(pageable);
    }

    @Override
    public Page<Event> findAllPastEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByEndTimeIsNotNull(pageable);
    }

    @Override
    public Page<Event> findAllVisibleEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByIsVisibleIsTrue(pageable);
    }

    @Override
    public Page<Event> findAllInvisibleEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByIsVisibleIsFalse(pageable);
    }

    @Override
    public Page<Event> findAllEventsByType(String typeID, int page, int size) {
        Type type = typeService.findByNameOrId(typeID);
        if(type == null){
            System.out.println("Type not found");
            return null;
        }
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByTypeId(type.getId(), pageable);
    }

    @Override
    public Page<Event> findAllEventsByLocation(String locationID, int page, int size) {
        Location location = locationService.findByIdOrName(locationID);
        if(location == null){
            System.out.println("Location not found");
            return null;
        }
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findByLocationId(location.getId(), pageable);
    }

    @Override
    public Event findById(UUID id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean deleteEvent(UUID eventId) throws Exception {
        try{
            Event toDelete = eventRepository.findById(eventId).orElse(null);
            if(toDelete != null){
                eventRepository.delete(toDelete);
                return true;
            }
            else{
                throw new Exception("Event not found");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean toggleVisibility(UUID eventId) throws Exception {
        try{
            Event toToggle = eventRepository.findById(eventId).orElse(null);
            if(toToggle != null){
                toToggle.setIsVisible(!toToggle.getIsVisible());
                eventRepository.save(toToggle);
                return true;
            }
            else{
                throw new Exception("Event not found");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    
}
