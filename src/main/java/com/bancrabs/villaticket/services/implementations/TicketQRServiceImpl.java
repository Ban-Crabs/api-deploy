package com.bancrabs.villaticket.services.implementations;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.response.VerifyDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterTicketQRDTO;
import com.bancrabs.villaticket.models.entities.QR;
import com.bancrabs.villaticket.models.entities.Ticket;
import com.bancrabs.villaticket.models.entities.TicketQR;
import com.bancrabs.villaticket.repositories.TicketQRRepository;
import com.bancrabs.villaticket.services.QRService;
import com.bancrabs.villaticket.services.TicketQRService;
import com.bancrabs.villaticket.services.TicketService;
import jakarta.transaction.Transactional;

@Service
public class TicketQRServiceImpl implements TicketQRService{

    @Autowired
    private TicketQRRepository ticketQRRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private QRService qrService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public VerifyDTO verify(Timestamp timestamp, RegisterTicketQRDTO data) throws Exception {
        try{
            Ticket ticket = ticketService.findById(data.getTicketId());
            if(ticket == null){
                throw new Exception("Ticket not found");
            }
            QR qr = qrService.findByCode(data.getQrCode());
            if(qr == null){
                throw new Exception("QR not found");
            }
            if(ticket.getResult()){
                ticketQRRepository.save(new TicketQR(timestamp, ticket, qr));
                return new VerifyDTO(false, "Ticket already redeemed");
            }
            TicketQR check = ticketQRRepository.findByTicketIdAndQrId(data.getTicketId(), qr.getId());
            if(check != null && ticket.getResult() != null){
                ticketQRRepository.save(new TicketQR(timestamp, ticket, qr));
                return new VerifyDTO(false, "Ticket redeem has already been attempted with this QR");
            }
            if(timestamp.getTime() - qr.getCreationTime().getTime() > 600000 || timestamp.getTime() - qr.getCreationTime().getTime() <= 0){
                ticket.setResult(false);
                ticketService.save(ticket);
                ticketQRRepository.save(new TicketQR(timestamp, ticket, qr));
                return new VerifyDTO(false, "QR expired");
            }
            else{
                ticket.setResult(true);
                ticketService.save(ticket);
                ticketQRRepository.save(new TicketQR(timestamp, ticket, qr));
                return new VerifyDTO(true, "Ticket redeemed");
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(UUID id) throws Exception {
        try{
            TicketQR check = ticketQRRepository.findById(id).orElse(null);
            if(check == null){
                throw new Exception("TicketQR not found");
            }
            ticketQRRepository.delete(check);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<TicketQR> findAll() {
        return ticketQRRepository.findAll();
    }

    @Override
    public List<TicketQR> findByTicketId(UUID ticketId) {
        return ticketQRRepository.findByTicketId(ticketId);
    }
}
