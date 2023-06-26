package com.bancrabs.villaticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.CreateTicketDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterTicketDTO;
import com.bancrabs.villaticket.models.entities.Order;
import com.bancrabs.villaticket.models.entities.Ticket;
import com.bancrabs.villaticket.models.entities.TicketRegister;
import com.bancrabs.villaticket.repositories.TicketRegisterRepository;
import com.bancrabs.villaticket.services.OrderService;
import com.bancrabs.villaticket.services.TicketRegisterService;
import com.bancrabs.villaticket.services.TicketService;

@Service
public class TicketRegisterImpl implements TicketRegisterService{

    @Autowired
    private TicketRegisterRepository ticketRegisterRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private OrderService orderService;

    @Override
    public Boolean save(RegisterTicketDTO data) throws Exception {
        try{
            Ticket boughtTicket = ticketService.findById(data.getTicketId());
            if(boughtTicket == null){
                throw new Exception("Ticket not found");
            }
            Order relatedOrder = orderService.findById(data.getOrderId());
            if(relatedOrder == null){
                throw new Exception("Order not found");
            }
            ticketService.update(boughtTicket.getId(), new CreateTicketDTO(null ,relatedOrder.getUser().getUsername()));
            ticketRegisterRepository.save(new TicketRegister(boughtTicket, relatedOrder));
            return true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean delete(UUID id) throws Exception {
        try{
            TicketRegister toDelete = ticketRegisterRepository.findById(id).orElse(null);
            if(toDelete == null){
                throw new Exception("TicketRegister not found");
            }
            ticketRegisterRepository.delete(toDelete);
            return true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<TicketRegister> findAll() {
        return ticketRegisterRepository.findAll();
    }

    @Override
    public TicketRegister findById(UUID id) {
        return ticketRegisterRepository.findById(id).orElse(null);
    }

    @Override
    public TicketRegister findByTicketId(UUID ticketId) {
        return ticketRegisterRepository.findByTicketId(ticketId);
    }

    @Override
    public TicketRegister findByOrderId(UUID orderId) {
        return ticketRegisterRepository.findByOrderId(orderId);
    }
    
}
