package com.bancrabs.villaticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.SaveTierDTO;
import com.bancrabs.villaticket.models.entities.Locale;
import com.bancrabs.villaticket.models.entities.Tier;
import com.bancrabs.villaticket.repositories.TierRepository;
import com.bancrabs.villaticket.services.LocaleService;
import com.bancrabs.villaticket.services.TierService;

import jakarta.transaction.Transactional;

@Service
public class TierServiceImpl implements TierService{

    @Autowired
    private TierRepository tierRepository;

    @Autowired
    private LocaleService localeService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean save(SaveTierDTO data) throws Exception {
        try{
            Locale locale = localeService.findById(data.getLocaleId());
            if(locale == null){
                throw new Exception("Locale not found");
            }
            Tier check = tierRepository.findByNameAndLocaleId(data.getName(), data.getLocaleId());
            if(check == null){
                tierRepository.save(new Tier(data.getName(), data.getPrice(), data.getQuantity(), locale));
            }
            else{
                check.setName(data.getName());
                check.setPrice(data.getPrice());
                check.setQuantity(data.getQuantity());
                check.setLocale(locale);
                tierRepository.save(check);
            }
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(UUID id) throws Exception {
        try{
            Tier toDelete = tierRepository.findById(id).orElse(null);
            if(toDelete == null){
                throw new Exception("Tier not found");
            }
            tierRepository.delete(toDelete);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Tier findById(UUID id) {
        return tierRepository.findById(id).orElse(null);
    }

    @Override
    public List<Tier> findAll() {
        return tierRepository.findAll();
    }

    @Override
    public List<Tier> findByLocale(String localeId) {
        return tierRepository.findByLocaleId(localeId);
    }
}
