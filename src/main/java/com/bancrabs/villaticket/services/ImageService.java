package com.bancrabs.villaticket.services;

import java.util.List;
import java.util.UUID;

import com.bancrabs.villaticket.models.dtos.save.SaveImageDTO;
import com.bancrabs.villaticket.models.entities.Image;

public interface ImageService {
    Boolean save(SaveImageDTO data) throws Exception;
    Boolean delete(SaveImageDTO data) throws Exception;

    List<Image> findAll();
    Image findById(String id);
    List<Image> findAllByEvent(UUID eventId);
}
