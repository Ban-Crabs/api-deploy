package com.bancrabs.villaticket.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancrabs.villaticket.models.dtos.save.SaveImageDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveLocationDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveEventAuxDTO;
import com.bancrabs.villaticket.services.CategoryService;
import com.bancrabs.villaticket.services.ImageService;
import com.bancrabs.villaticket.services.LocationService;
import com.bancrabs.villaticket.services.TypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/eventaux")
public class EventAuxController {
    
    @Autowired
    private TypeService typeService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/type")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> createType(@ModelAttribute @Valid SaveEventAuxDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            if(typeService.save(data)){
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/location")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> createLocation(@ModelAttribute @Valid SaveLocationDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            if(locationService.save(data)){
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/image")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> createImage(@ModelAttribute @Valid SaveImageDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            if(imageService.save(data)){
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "Event not found":
                    return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PostMapping("/category")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> createCategory(@ModelAttribute @Valid SaveEventAuxDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            if(categoryService.save(data)){
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "Event not found":
                    return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/type")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getAllTypes(){
        try{
            return new ResponseEntity<>(typeService.findAll(), HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/location")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getAllLocations(){
        try{
            return new ResponseEntity<>(locationService.findAll(), HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/image")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getAllImages(){
        try{
            return new ResponseEntity<>(imageService.findAll(), HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getAllCategories(){
        try{
            return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
