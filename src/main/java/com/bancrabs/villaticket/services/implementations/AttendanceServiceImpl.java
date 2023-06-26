package com.bancrabs.villaticket.services.implementations;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.RecordAttendanceDTO;
import com.bancrabs.villaticket.models.entities.Attendance;
import com.bancrabs.villaticket.models.entities.Event;
import com.bancrabs.villaticket.models.entities.User;
import com.bancrabs.villaticket.repositories.AttendanceRepository;
import com.bancrabs.villaticket.services.AttendanceService;
import com.bancrabs.villaticket.services.EventService;
import com.bancrabs.villaticket.services.UserService;

import jakarta.transaction.Transactional;

@Service
public class AttendanceServiceImpl implements AttendanceService{

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean save(RecordAttendanceDTO data) throws Exception {
        try{
            User attendee = userService.findById(data.getUserId());
            if(attendee == null){
                throw new Exception("User not found");
            }
            Event related = eventService.findById(data.getEventId());
            if(related == null){
                throw new Exception("Event not found");
            }
            attendanceRepository.save(new Attendance(attendee, related));
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Page<Attendance> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attendanceRepository.findAll(pageable);
    }

    @Override
    public Page<Attendance> findByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attendanceRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Attendance> findByEventId(UUID eventId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attendanceRepository.findByEventId(eventId, pageable);
    }
    
}
