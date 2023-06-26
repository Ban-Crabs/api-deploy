package com.bancrabs.villaticket.services;

import java.util.List;
import java.util.UUID;

import com.bancrabs.villaticket.models.dtos.save.RegisterTicketDTO;
import com.bancrabs.villaticket.models.entities.TicketRegister;

public interface TicketRegisterService {
    Boolean save(RegisterTicketDTO data) throws Exception;
    Boolean delete(UUID id) throws Exception;

    List<TicketRegister> findAll();
    TicketRegister findById(UUID id);
    TicketRegister findByTicketId(UUID ticketId);
    TicketRegister findByOrderId(UUID orderId);
}
