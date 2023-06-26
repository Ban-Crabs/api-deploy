package com.bancrabs.villaticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.CreateTicketDTO;
import com.bancrabs.villaticket.models.entities.Ticket;
import com.bancrabs.villaticket.models.entities.Tier;
import com.bancrabs.villaticket.models.entities.User;
import com.bancrabs.villaticket.repositories.TicketRepository;
import com.bancrabs.villaticket.services.TicketService;
import com.bancrabs.villaticket.services.TierService;
import com.bancrabs.villaticket.services.UserService;

@Service
public class TicketServiceImpl implements TicketService{

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TierService tierService;

    @Autowired
    private UserService userService;

    @Override
    public Boolean save(UUID tierId) throws Exception {
        try{
            Tier relatedTier = tierService.findById(tierId);
            if(relatedTier == null){
                throw new Exception("Tier not found");
            }
            List<Ticket> tickets = ticketRepository.findAllByTierId(relatedTier.getId());
            if(tickets != null){
                if(tickets.size() >= relatedTier.getQuantity()){
                    return false;
                }
            }
            ticketRepository.save(new Ticket(relatedTier, null));
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Boolean delete(UUID id) throws Exception {
        try{
            Ticket toDelete = ticketRepository.findById(id).orElse(null);
            if(toDelete == null){
                throw new Exception("Ticket not found");
            }
            ticketRepository.deleteById(id);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Page<Ticket> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findAll(pageable);
    }

    @Override
    public Ticket findById(UUID id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Ticket> findByTierId(UUID tierId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findByTierId(tierId, pageable);
    }

    @Override
    public Ticket findByUserId(UUID userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Override
    public Boolean save(Ticket ticket) {
        try{
            ticketRepository.save(ticket);
            return true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean update(UUID id, CreateTicketDTO data) throws Exception {
        try{
            Ticket toUpdate = ticketRepository.findById(id).orElse(null);
            if(toUpdate == null){
                throw new Exception("Ticket not found");
            }
            if(toUpdate.getResult() != null && toUpdate.getResult()){
                throw new Exception("Ticket already redeemed");
            }
            if(data.getTierId() == null){
                User relatedUser = userService.findById(data.getUserId());
                if(relatedUser == null){
                    throw new Exception("User not found");
                }
                toUpdate.setUser(relatedUser);
                ticketRepository.save(toUpdate);
                return true;
            }
            else{
                Tier relatedTier = tierService.findById(data.getTierId());
                if(relatedTier == null){
                    throw new Exception("Tier not found");
                }
                User relatedUser = userService.findById(data.getUserId());
                if(relatedUser == null){
                    throw new Exception("User not found");
                }
                toUpdate.setTier(relatedTier);
                toUpdate.setUser(relatedUser);
                ticketRepository.save(toUpdate);
                return true;
            }
        }
        catch(Exception e){
            throw e;
        }
    }
    
}
