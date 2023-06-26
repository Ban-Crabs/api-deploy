package com.bancrabs.villaticket.services;

import java.util.List;
import java.util.UUID;

import com.bancrabs.villaticket.models.dtos.response.UserPrivilegeResponseDTO;
import com.bancrabs.villaticket.models.dtos.save.SavePrivilegeDTO;
import com.bancrabs.villaticket.models.entities.UserPrivilege;

public interface UserPrivilegeService {
    Boolean save(SavePrivilegeDTO data) throws Exception;
    Boolean delete(SavePrivilegeDTO data) throws Exception;

    List<UserPrivilege> findAll();
    List<UserPrivilegeResponseDTO> findByUserId(String id);
    List<UserPrivilege> findByName(String name);
    UserPrivilege findById(UUID id);
}
