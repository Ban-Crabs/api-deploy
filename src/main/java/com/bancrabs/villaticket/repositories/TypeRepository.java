package com.bancrabs.villaticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Type;

public interface TypeRepository extends JpaRepository<Type, String>{
    Type findByNameOrId(String name, String id);
}
