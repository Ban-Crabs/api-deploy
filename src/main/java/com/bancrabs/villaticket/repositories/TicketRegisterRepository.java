package com.bancrabs.villaticket.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.TicketRegister;

public interface TicketRegisterRepository extends JpaRepository<TicketRegister, UUID>{
    TicketRegister findByTicketId(UUID ticketId);
    TicketRegister findByOrderId(UUID orderId);
}
