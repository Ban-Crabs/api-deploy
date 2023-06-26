package com.bancrabs.villaticket.services;

import java.util.List;

import com.bancrabs.villaticket.models.dtos.save.SaveEventAuxDTO;
import com.bancrabs.villaticket.models.entities.Type;

public interface TypeService {
    Boolean save(SaveEventAuxDTO data) throws Exception;
    Boolean delete(String identifier) throws Exception;
    
    List<Type> findAll();
    Type findByNameOrId(String identifier);
}
