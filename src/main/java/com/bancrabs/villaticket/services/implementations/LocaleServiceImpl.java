package com.bancrabs.villaticket.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.SaveLocaleDTO;
import com.bancrabs.villaticket.models.entities.Locale;
import com.bancrabs.villaticket.repositories.LocaleRepository;
import com.bancrabs.villaticket.services.LocaleService;

import jakarta.transaction.Transactional;

@Service
public class LocaleServiceImpl implements LocaleService{

    @Autowired
    private LocaleRepository localeRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean save(SaveLocaleDTO data) throws Exception {
        try{
            Locale check = localeRepository.findById(data.getId()).orElse(null);
            if(check == null){
                localeRepository.save(new Locale(data.getId(), data.getName()));
            }
            else{
                check.setName(data.getName());
                localeRepository.save(check);
            }
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Boolean delete(String id) throws Exception {
        try{
            Locale toDelete = localeRepository.findById(id).orElse(null);
            if(toDelete == null){
                throw new Exception("Locale not found");
            }
            localeRepository.delete(toDelete);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<Locale> findAll() {
        return localeRepository.findAll();
    }

    @Override
    public Locale findById(String id) {
        return localeRepository.findById(id).orElse(null);
    }
    
}
