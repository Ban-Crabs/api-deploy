package com.bancrabs.villaticket.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID>{
    Page<Ticket> findByTierId(UUID tierId, Pageable pageable);
    List<Ticket> findAllByTierId(UUID tierId);
    Ticket findByUserId(UUID userId);
}
