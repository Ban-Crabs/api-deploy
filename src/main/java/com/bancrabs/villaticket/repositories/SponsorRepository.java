package com.bancrabs.villaticket.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Sponsor;

public interface SponsorRepository extends JpaRepository<Sponsor, String>{
    List<Sponsor> findAllByEventId(UUID eventId);
}
