package com.bancrabs.villaticket.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bancrabs.villaticket.models.dtos.response.IdResponseDTO;
import com.bancrabs.villaticket.models.dtos.response.PageResponseDTO;
import com.bancrabs.villaticket.models.dtos.response.TicketResponseDTO;
import com.bancrabs.villaticket.models.dtos.response.UserResponseDTO;
import com.bancrabs.villaticket.models.dtos.response.VerifyDTO;
import com.bancrabs.villaticket.models.dtos.save.CreateTicketDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterOrderDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterTicketDTO;
import com.bancrabs.villaticket.models.dtos.save.RegisterTicketQRDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveTicketTransferDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveTierDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveTransferDTO;
import com.bancrabs.villaticket.models.dtos.save.VerifyTransferDTO;
import com.bancrabs.villaticket.models.entities.Ticket;
import com.bancrabs.villaticket.models.entities.Transfer;
import com.bancrabs.villaticket.services.OrderService;
import com.bancrabs.villaticket.services.TicketQRService;
import com.bancrabs.villaticket.services.TicketRegisterService;
import com.bancrabs.villaticket.services.TicketService;
import com.bancrabs.villaticket.services.TicketTransferService;
import com.bancrabs.villaticket.services.TierService;
import com.bancrabs.villaticket.services.TransferService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;

    @Autowired
    private TierService tierService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private TicketTransferService ticketTransferService;

    @Autowired
    private TicketRegisterService ticketRegisterService;

    @Autowired
    private TicketQRService ticketQRService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> createTicket(@ModelAttribute UUID tierId){
        try{
            if(ticketService.save(tierId)){
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Tier sold out", HttpStatus.CONFLICT);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "Tier not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "User not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PostMapping("/order/{id}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> orderTicket(@PathVariable("id") UUID ticketId, @ModelAttribute @Valid RegisterOrderDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            UUID newOrder = orderService.save(data);
            if(newOrder != null){
                ticketRegisterService.save(new RegisterTicketDTO(ticketId, newOrder));
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Couldn't place order", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "User not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getAll(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "amt", defaultValue = "10") int size){
        try{
            Page<Ticket> rawTickets = ticketService.findAll(page, size);
            List<TicketResponseDTO> tickets = new ArrayList<>();
            rawTickets.getContent().forEach(ticket->{
                if(ticket.getUser() == null)
                    tickets.add(new TicketResponseDTO(ticket.getId(), ticket.getTier().getId(), null, ticket.getResult()));
                else
                    tickets.add(new TicketResponseDTO(ticket.getId(), ticket.getTier().getId(), new UserResponseDTO(ticket.getUser().getUsername(), ticket.getUser().getEmail()), ticket.getResult()));
            });
            PageResponseDTO<TicketResponseDTO> response = new PageResponseDTO<>(tickets, rawTickets.getTotalPages(), rawTickets.getTotalElements());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getById(@PathVariable("id") UUID id){
        try{
            Ticket rawTicket = ticketService.findById(id);
            TicketResponseDTO ticket = new TicketResponseDTO(rawTicket.getId(), rawTicket.getTier().getId(), new UserResponseDTO(rawTicket.getUser().getUsername(), rawTicket.getUser().getEmail()), rawTicket.getResult());
            return new ResponseEntity<>(ticket, HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> updateTicket(@PathVariable("id") UUID id, @ModelAttribute @Valid CreateTicketDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            ticketService.update(id, data);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "Ticket not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "Tier not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "User not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "Ticket already redeemed":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> deleteTicket(@PathVariable("id") String id){
        try{
            UUID uuid = UUID.fromString(id);
            ticketService.delete(uuid);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "Ticket not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/tier")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getAllTiers(){
        try{
            return new ResponseEntity<>(tierService.findAll(), HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tier")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> createTier(@ModelAttribute @Valid SaveTierDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            tierService.save(data);
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "Locale not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> transferTicket(@ModelAttribute @Valid SaveTransferDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            Transfer transfer = transferService.save(data);
            return new ResponseEntity<>(new IdResponseDTO(transfer.getId()), HttpStatus.CREATED);
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "Ticket not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "Sender not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "Receiver not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "Ticket already redeemed":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PostMapping("/transfer/{id}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> transferTicket(@PathVariable("id") UUID transferId, @ModelAttribute @Valid VerifyTransferDTO req, @ModelAttribute @Valid SaveTicketTransferDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            Transfer check = transferService.findById(transferId);
            if(check == null){
                throw new Exception("Transfer not found");
            }
            VerifyDTO res = ticketTransferService.verify(check, req, data);
            if(res.getResult()){
                ticketService.update(data.getTicketId(), new CreateTicketDTO(null, req.getReceiverId()));
                return new ResponseEntity<>("Verified", HttpStatus.OK);
            }
            else{
                switch(res.getMessage()){
                    case "Transfer already completed":
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
                    case "Transfer reserved for another user":
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
                    case "Transfer already attempted with this QR":
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);    
                    case "QR expired":
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.BAD_REQUEST);
                    case "Tier not found":
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);
                    case "Ticket not found":
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);
                    default:
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.BAD_REQUEST);
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "Ticket not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "QR not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "Transfer not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PostMapping("/redeem")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<?> redeemTicket(@ModelAttribute @NotEmpty Timestamp timestamp, @ModelAttribute @Valid RegisterTicketQRDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            VerifyDTO res = ticketQRService.verify(timestamp, data);
            if(res.getResult()){
                return new ResponseEntity<>("Redeemed", HttpStatus.OK);
            }
            else{
                switch(res.getMessage()){
                    case "Ticket already redeemed":
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
                    case "Ticket redeem has already been attempted with this QR":
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
                    case "QR expired":
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.BAD_REQUEST);
                    default:
                        return new ResponseEntity<>(res.getMessage(), HttpStatus.BAD_REQUEST);
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
            switch(e.getMessage()){
                case "Ticket not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                case "QR not found":
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                default:
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
