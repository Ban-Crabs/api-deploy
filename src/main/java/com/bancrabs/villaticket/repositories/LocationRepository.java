package com.bancrabs.villaticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Location;

public interface LocationRepository extends JpaRepository<Location, String>{
    Location findByIdOrName(String id, String name);
}
