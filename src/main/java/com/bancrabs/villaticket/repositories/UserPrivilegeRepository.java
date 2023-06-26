package com.bancrabs.villaticket.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.UserPrivilege;

public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, UUID>{
    UserPrivilege findByNameAndUserId(String name, UUID userId);
    List<UserPrivilege> findByUserId(UUID userId);
    List<UserPrivilege> findByName(String name);
    List<UserPrivilege> findByNameOrderByIdAsc(String name);
    List<UserPrivilege> findByNameOrderByIdDesc(String name);
}