package com.bancrabs.villaticket.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.bancrabs.villaticket.models.dtos.response.VerifyDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterTicketQRDTO;
import com.bancrabs.villaticket.models.entities.TicketQR;

public interface TicketQRService {
    VerifyDTO verify(Timestamp timestamp, RegisterTicketQRDTO data) throws Exception;
    Boolean delete(UUID id) throws Exception;

    List<TicketQR> findAll();
    List<TicketQR> findByTicketId(UUID ticketId);
}
