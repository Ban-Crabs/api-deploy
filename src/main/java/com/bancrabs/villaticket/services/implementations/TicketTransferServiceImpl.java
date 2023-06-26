package com.bancrabs.villaticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.response.VerifyDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveTicketTransferDTO;
import com.bancrabs.villaticket.models.dtos.save.VerifyTransferDTO;
import com.bancrabs.villaticket.models.entities.QR;
import com.bancrabs.villaticket.models.entities.Ticket;
import com.bancrabs.villaticket.models.entities.TicketTransfer;
import com.bancrabs.villaticket.models.entities.Transfer;
import com.bancrabs.villaticket.repositories.TicketTransferRepository;
import com.bancrabs.villaticket.services.QRService;
import com.bancrabs.villaticket.services.TicketService;
import com.bancrabs.villaticket.services.TicketTransferService;
import com.bancrabs.villaticket.services.TransferService;
import com.bancrabs.villaticket.services.UserService;

import jakarta.transaction.Transactional;

@Service
public class TicketTransferServiceImpl implements TicketTransferService {

    @Autowired
    private TicketTransferRepository ticketTransferRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private QRService qrService;

    @Autowired
    private TicketService ticketService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public VerifyDTO verify(Transfer transfer, VerifyTransferDTO req, SaveTicketTransferDTO data) throws Exception {
        try {
            Ticket ticket = ticketService.findById(data.getTicketId());
            if (ticket == null) {
                throw new Exception("Ticket not found");
            }
            QR qr = qrService.findByCode(data.getQrCode());
            if (qr == null) {
                throw new Exception("QR not found");
            }
            if (transfer.getResult() != null && transfer.getResult()) {
                ticketTransferRepository.save(new TicketTransfer(req.getTimestamp(), qr, transfer));
                return new VerifyDTO(false, "Transfer already completed");
            }
            TicketTransfer check = ticketTransferRepository.findByTransferIdAndQrId(transfer.getId(), qr.getId());
            if (check != null && transfer.getResult() != null) {
                //The transfer has already been attempted before and therefore this attempt should only be logged
                ticketTransferRepository.save(new TicketTransfer(req.getTimestamp(), qr, transfer));
                return new VerifyDTO(false, "Transfer already attempted with this QR");
            }
            else if(transfer.getReceiver() != null && transfer.getReceiver().getId() != userService.findById(req.getReceiverId()).getId()){
                return new VerifyDTO(false, "Transfer reserved for another user");
            }
            if (req.getTimestamp().getTime() - qr.getCreationTime().getTime() > 600000 || req.getTimestamp().getTime() - qr.getCreationTime().getTime() <= 0) {
                transfer.setResult(false);
                transfer.setReceiver(null);
                transferService.save(transfer);
                ticketTransferRepository.save(new TicketTransfer(req.getTimestamp(), qr, transfer));
                return new VerifyDTO(false, "QR expired");
            }
            else{
                transfer.setResult(true);
                transfer.setReceiver(userService.findById(req.getReceiverId()));
                transferService.save(transfer);
                ticket.setUser(transfer.getReceiver());
                ticketService.save(ticket);
                ticketTransferRepository.save(new TicketTransfer(req.getTimestamp(), qr, transfer));
                return new VerifyDTO(true, "Transfer completed");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(UUID id) throws Exception {
        try {
            TicketTransfer ticketTransfer = ticketTransferRepository.findById(id).orElse(null);
            if (ticketTransfer == null) {
                throw new Exception("TicketTransfer not found");
            }
            ticketTransferRepository.delete(ticketTransfer);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<TicketTransfer> findAll() {
        return ticketTransferRepository.findAll();
    }

    @Override
    public TicketTransfer findById(UUID id) {
        return ticketTransferRepository.findById(id).orElse(null);
    }

}
