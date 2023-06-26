package com.bancrabs.villaticket.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Image;

public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findByEventId(UUID eventId);
}
