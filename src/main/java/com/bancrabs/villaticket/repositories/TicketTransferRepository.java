package com.bancrabs.villaticket.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.TicketTransfer;

public interface TicketTransferRepository extends JpaRepository<TicketTransfer, UUID>{
    TicketTransfer findByTransferIdAndQrId(UUID transferId, UUID qrId);
}
