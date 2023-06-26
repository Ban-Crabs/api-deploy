package com.bancrabs.villaticket.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.bancrabs.villaticket.models.dtos.save.CreateTicketDTO;
import com.bancrabs.villaticket.models.entities.Ticket;

public interface TicketService {
    Boolean save(UUID tierId) throws Exception;
    Boolean save(Ticket ticket);
    Boolean delete(UUID id) throws Exception;
    Boolean update(UUID id, CreateTicketDTO data) throws Exception;

    Page<Ticket> findAll(int page, int size);
    Ticket findById(UUID id);
    Page<Ticket> findByTierId(UUID tierId, int page, int size);
    Ticket findByUserId(UUID userId);
}
