package com.bancrabs.villaticket.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bancrabs.villaticket.models.dtos.LoginDTO;
import com.bancrabs.villaticket.models.dtos.response.PageResponseDTO;
import com.bancrabs.villaticket.models.dtos.response.TokenDTO;
import com.bancrabs.villaticket.models.dtos.response.UserResponseDTO;
import com.bancrabs.villaticket.models.dtos.save.RecordAttendanceDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterUserDTO;
import com.bancrabs.villaticket.models.dtos.save.SavePrivilegeDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveUserDTO;
import com.bancrabs.villaticket.models.entities.Attendance;
import com.bancrabs.villaticket.models.entities.Token;
import com.bancrabs.villaticket.models.entities.User;
import com.bancrabs.villaticket.services.AttendanceService;
import com.bancrabs.villaticket.services.UserPrivilegeService;
import com.bancrabs.villaticket.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserPrivilegeService userPrivilegeService;

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute @Valid LoginDTO data, BindingResult result) {
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }

            if(userService.login(data)){
                User user = userService.findById(data.getId());
                try {
                    Token token = userService.registerToken(user);
                    return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else{
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "User not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "Wrong password":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute @Valid RegisterUserDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }

            if(userService.register(data)){
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "User already exists":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id){
        try{
            if(userService.deleteById(id)){
                return new ResponseEntity<>("Deleted", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "User not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @ModelAttribute @Valid SaveUserDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }

            if(userService.update(data, id)){
                return new ResponseEntity<>("Updated", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "User not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> findById(@PathVariable("id") String id){
        try{
            User user = userService.findById(id);
            if(user != null){
                return new ResponseEntity<>(new UserResponseDTO(user.getUsername(), user.getEmail()), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> findAll(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "amt", defaultValue = "10") int size){
        try{
            Page<User> rawUsers = userService.findAll(page, size);
            List<UserResponseDTO> users = new ArrayList<>();
            rawUsers.forEach(us->{
                users.add(new UserResponseDTO(us.getUsername(), us.getEmail()));
            });
            PageResponseDTO<UserResponseDTO> response = new PageResponseDTO<>(users, rawUsers.getTotalPages(), rawUsers.getTotalElements());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/privilege")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> addPrivilege(@ModelAttribute(name = "userId") String id, @ModelAttribute(name = "privName") String privName){
        try{
            User user = userService.findById(id);
            if(user == null){
                return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
            }

            if(userPrivilegeService.save(new SavePrivilegeDTO(privName, user.getId()))){
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "User not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "Privilege already exists":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/{id}/privilege")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getPrivileges(@PathVariable("id") String id){
        try{
            return new ResponseEntity<>(userPrivilegeService.findByUserId(id), HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/privilege")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getAllPrivileges(){
        try{
            return new ResponseEntity<>(userPrivilegeService.findAll(), HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/attendance")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> attendEvent(@ModelAttribute(name="userId") String id, @ModelAttribute(name="eventId") UUID eventId){
        try{
            if(attendanceService.save(new RecordAttendanceDTO(id, eventId))){
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Conflict", HttpStatus.CONFLICT);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "User not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "Event not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/{id}/attendance")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getAttendance(@PathVariable("id") String id, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "amt", defaultValue = "10") int size){
        try{
            User user = userService.findById(id);
            if(user == null){
                return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
            }
            if(!userService.verifyIdentity(id)) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            Page<Attendance> rawAttendance = attendanceService.findByUserId(user.getId(), page, size);
            PageResponseDTO<Attendance> response = new PageResponseDTO<>(rawAttendance.getContent(), rawAttendance.getTotalPages(), rawAttendance.getTotalElements());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/attendance")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getAllAttendance(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "amt", defaultValue = "10") int size){
        try{
            Page<Attendance> rawAttendance = attendanceService.findAll(page, size);
            PageResponseDTO<Attendance> response = new PageResponseDTO<>(rawAttendance.getContent(), rawAttendance.getTotalPages(), rawAttendance.getTotalElements());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
