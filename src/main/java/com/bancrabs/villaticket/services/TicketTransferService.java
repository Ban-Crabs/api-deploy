package com.bancrabs.villaticket.services;

import java.util.List;
import java.util.UUID;

import com.bancrabs.villaticket.models.dtos.response.VerifyDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveTicketTransferDTO;
import com.bancrabs.villaticket.models.dtos.save.VerifyTransferDTO;
import com.bancrabs.villaticket.models.entities.TicketTransfer;
import com.bancrabs.villaticket.models.entities.Transfer;

public interface TicketTransferService {
    Boolean delete(UUID id) throws Exception;
    VerifyDTO verify(Transfer transfer, VerifyTransferDTO req, SaveTicketTransferDTO data) throws Exception;

    List<TicketTransfer> findAll();
    TicketTransfer findById(UUID id);
}
