package com.bancrabs.villaticket.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.response.UserPrivilegeResponseDTO;
import com.bancrabs.villaticket.models.dtos.save.SavePrivilegeDTO;
import com.bancrabs.villaticket.models.entities.User;
import com.bancrabs.villaticket.models.entities.UserPrivilege;
import com.bancrabs.villaticket.repositories.UserPrivilegeRepository;
import com.bancrabs.villaticket.services.UserPrivilegeService;
import com.bancrabs.villaticket.services.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserPrivilegeServiceImpl implements UserPrivilegeService{

    @Autowired
    private UserPrivilegeRepository userPrivilegeRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean save(SavePrivilegeDTO data) throws Exception {
        try{
            User related = userService.findById(data.getUserId());
            if(related == null){
                throw new Exception("User not found");
            }
            UserPrivilege check = userPrivilegeRepository.findByNameAndUserId(data.getName(), data.getUserId());
            if(check == null){
                userPrivilegeRepository.save(new UserPrivilege(data.getName(), related));
            }
            else{
                throw new Exception("Privilege already exists");
            }
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(SavePrivilegeDTO data) throws Exception {
        try{
            UserPrivilege check = userPrivilegeRepository.findByNameAndUserId(data.getName(), data.getUserId());
            if(check != null){
                userPrivilegeRepository.delete(check);
                return true;
            }
            else{
                throw new Exception("Privilege not found");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<UserPrivilege> findAll() {
        return userPrivilegeRepository.findAll();
    }

    @Override
    public List<UserPrivilegeResponseDTO> findByUserId(String userId) {
        User user = userService.findById(userId);
        if(user == null){
            return null;
        }
        List<UserPrivilege> privileges = userPrivilegeRepository.findByUserId(user.getId());
        if(privileges != null){
            List<UserPrivilegeResponseDTO> response = new ArrayList<>();
            privileges.forEach(privilege -> {
                response.add(new UserPrivilegeResponseDTO(privilege.getName(), userId));
            });
            return response;
        }
        else{
            return null;
        }
    }

    @Override
    public List<UserPrivilege> findByName(String name) {
        return userPrivilegeRepository.findByName(name);
    }

    @Override
    public UserPrivilege findById(UUID id) {
        return userPrivilegeRepository.findById(id).orElse(null);
    }
    
}
