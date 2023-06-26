package com.bancrabs.villaticket.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.SaveEventAuxDTO;
import com.bancrabs.villaticket.models.entities.Type;
import com.bancrabs.villaticket.repositories.TypeRepository;
import com.bancrabs.villaticket.services.TypeService;

import jakarta.transaction.Transactional;

@Service
public class TypeServiceImpl implements TypeService{

    @Autowired
    private TypeRepository typeRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean save(SaveEventAuxDTO data) throws Exception{
        try{
            Type check = typeRepository.findByNameOrId(data.getName(), data.getCode());
            if(check == null){
                typeRepository.save(new Type(data.getCode(), data.getName()));
                return true;
            }
            else{
                check.setId(data.getCode());
                check.setName(data.getName());
                typeRepository.save(check);
                return true;
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Type findByNameOrId(String identifier) {
        return typeRepository.findByNameOrId(identifier, identifier);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(String identifier) throws Exception {
        try{
            Type toDelete = typeRepository.findByNameOrId(identifier, identifier);
            if(toDelete == null){
                throw new Exception("Type not found");
            }
            typeRepository.delete(toDelete);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<Type> findAll() {
        return typeRepository.findAll();
    }
    
}
