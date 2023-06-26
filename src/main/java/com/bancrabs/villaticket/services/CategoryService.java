package com.bancrabs.villaticket.services;

import java.util.List;
import com.bancrabs.villaticket.models.dtos.save.SaveEventAuxDTO;
import com.bancrabs.villaticket.models.entities.Category;
import com.bancrabs.villaticket.models.entities.Event;

public interface CategoryService {
    Boolean save(SaveEventAuxDTO data) throws Exception;
    Boolean delete(SaveEventAuxDTO data) throws Exception;

    List<Category> findAll();
    Category findById(String id);
    List<Event> findEventsById(String id);
    List<Category> findByName(String name);
}
