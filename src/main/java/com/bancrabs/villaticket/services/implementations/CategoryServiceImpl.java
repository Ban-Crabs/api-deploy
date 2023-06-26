package com.bancrabs.villaticket.services.implementations;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.SaveEventAuxDTO;
import com.bancrabs.villaticket.models.entities.Category;
import com.bancrabs.villaticket.models.entities.Event;
import com.bancrabs.villaticket.repositories.CategoryRepository;
import com.bancrabs.villaticket.services.CategoryService;
import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean save(SaveEventAuxDTO data) throws Exception {
        try{
            Category check = categoryRepository.findById(data.getCode()).orElse(null);
            if(check == null){
                categoryRepository.save(new Category(data.getCode(), data.getName()));
            }
            else{
                check.setId(data.getCode());
                check.setName(data.getName());
                categoryRepository.save(check);
            }
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(SaveEventAuxDTO data) throws Exception {
        try{
            Category check = categoryRepository.findById(data.getCode()).orElse(null);
            if(check != null){
                categoryRepository.delete(check);
                return true;
            }
            else{
                throw new Exception("Category not found");
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Event> findEventsById(String id) {
        return categoryRepository.findById(id).orElse(null).getEvents();
    }
    
}
