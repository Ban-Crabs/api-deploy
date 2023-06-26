package com.bancrabs.villaticket.services;

import java.util.List;

import com.bancrabs.villaticket.models.dtos.save.SaveLocationDTO;
import com.bancrabs.villaticket.models.entities.Location;

public interface LocationService {
    Boolean save(SaveLocationDTO data) throws Exception;
    Boolean deleteLocation(String id) throws Exception;
    Boolean setAvailable(String id) throws Exception;
    Boolean setUnavailable(String id) throws Exception;

    Location findByIdOrName(String id);
    List<Location> findAll();
}
