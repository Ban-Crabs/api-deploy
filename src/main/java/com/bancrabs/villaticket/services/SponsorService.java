package com.bancrabs.villaticket.services;

import java.util.List;
import java.util.UUID;

import com.bancrabs.villaticket.models.dtos.save.SaveGenericDTO;
import com.bancrabs.villaticket.models.entities.Sponsor;

public interface SponsorService {
    Boolean save(SaveGenericDTO data) throws Exception;
    Boolean delete(SaveGenericDTO data) throws Exception;

    Sponsor findById(String id);
    List<Sponsor> findAll();
    List<Sponsor> findAllByEvent(UUID eventId);
}
