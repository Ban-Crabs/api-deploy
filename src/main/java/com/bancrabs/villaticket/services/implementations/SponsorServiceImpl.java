package com.bancrabs.villaticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.SaveGenericDTO;
import com.bancrabs.villaticket.models.entities.Event;
import com.bancrabs.villaticket.models.entities.Sponsor;
import com.bancrabs.villaticket.repositories.SponsorRepository;
import com.bancrabs.villaticket.services.EventService;
import com.bancrabs.villaticket.services.SponsorService;

import jakarta.transaction.Transactional;

@Service
public class SponsorServiceImpl implements SponsorService{

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private EventService eventService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean save(SaveGenericDTO data) throws Exception {
        try{
            Event related = eventService.findById(data.getEventId());
            if(related == null){
                throw new Exception("Event not found");
            }
            Sponsor check = sponsorRepository.findById(data.getCode()).orElse(null);
            if(check == null){
                sponsorRepository.save(new Sponsor(data.getCode(), data.getName(), related));
            }
            else{
                check.setName(data.getName());
                check.setEvent(related);
                sponsorRepository.save(check);
            }
            return true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(SaveGenericDTO data) throws Exception {
        try{
            Sponsor check = sponsorRepository.findById(data.getCode()).orElse(null);
            if(check != null){
                sponsorRepository.delete(check);
                return true;
            }
            else{
                throw new Exception("Sponsor not found");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Sponsor findById(String id) {
        return sponsorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Sponsor> findAll() {
        return sponsorRepository.findAll();
    }

    @Override
    public List<Sponsor> findAllByEvent(UUID eventId) {
        return sponsorRepository.findAllByEventId(eventId);
    }
    
}
