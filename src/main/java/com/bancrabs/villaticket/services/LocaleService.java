package com.bancrabs.villaticket.services;

import java.util.List;

import com.bancrabs.villaticket.models.dtos.save.SaveLocaleDTO;
import com.bancrabs.villaticket.models.entities.Locale;

public interface LocaleService {
    Boolean save(SaveLocaleDTO data) throws Exception;
    Boolean delete(String id) throws Exception;

    List<Locale> findAll();
    Locale findById(String id);
}
