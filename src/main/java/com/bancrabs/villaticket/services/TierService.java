package com.bancrabs.villaticket.services;

import java.util.List;
import java.util.UUID;

import com.bancrabs.villaticket.models.dtos.save.SaveTierDTO;
import com.bancrabs.villaticket.models.entities.Tier;

public interface TierService {
    Boolean save(SaveTierDTO data) throws Exception;
    Boolean delete(UUID id) throws Exception;

    Tier findById(UUID id);
    List<Tier> findAll();
    List<Tier> findByLocale(String localeId);
}
