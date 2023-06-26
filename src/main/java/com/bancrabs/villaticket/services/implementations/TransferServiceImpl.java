package com.bancrabs.villaticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.SaveTransferDTO;
import com.bancrabs.villaticket.models.entities.Ticket;
import com.bancrabs.villaticket.models.entities.Transfer;
import com.bancrabs.villaticket.models.entities.User;
import com.bancrabs.villaticket.repositories.TransferRepository;
import com.bancrabs.villaticket.services.TicketService;
import com.bancrabs.villaticket.services.TransferService;
import com.bancrabs.villaticket.services.UserService;

import jakarta.transaction.Transactional;

@Service
public class TransferServiceImpl implements TransferService{

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Transfer save(SaveTransferDTO data) throws Exception {
        try{
            User sender = userService.findById(data.getSenderId());
            if(sender == null){
                throw new Exception("Sender not found");
            }
            Ticket ticket = ticketService.findById(data.getTicketId());
            if(ticket == null){
                throw new Exception("Ticket not found");
            }
            if(ticket.getResult() != null && ticket.getResult()){
                throw new Exception("Ticket already redeemed");
            }
            if(data.getReceiverId() != null){
                User receiver = userService.findById(data.getReceiverId());
                if(receiver == null){
                    throw new Exception("Receiver not found");
                }
                return transferRepository.save(new Transfer(sender, receiver, ticket));
            }
            else{
                return transferRepository.save(new Transfer(sender, ticket));
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean save(Transfer data) throws Exception {
        try{
            transferRepository.save(data);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(UUID id) throws Exception {
        try{
            transferRepository.deleteById(id);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Transfer findById(UUID id) {
        return transferRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public List<Transfer> findBySenderId(UUID senderId) {
        return transferRepository.findBySenderId(senderId);
    }

    @Override
    public List<Transfer> findByReceiverId(UUID receiverId) {
        return transferRepository.findByReceiverId(receiverId);
    }

    @Override
    public List<Transfer> findBySenderIdAndReceiverId(UUID senderId, UUID receiverId) {
        return transferRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    @Override
    public List<Transfer> findByResultIsNotNull() {
        return transferRepository.findByResultIsNotNull();
    }

    @Override
    public List<Transfer> findByResultIsNull() {
        return transferRepository.findByResultIsNull();
    }

    @Override
    public List<Transfer> findByResultIsTrue() {
        return transferRepository.findByResultIsTrue();
    }

    @Override
    public List<Transfer> findByResultIsFalse() {
        return transferRepository.findByResultIsFalse();
    }
    
}
