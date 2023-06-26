package com.bancrabs.villaticket.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancrabs.villaticket.models.dtos.response.QRResponseDTO;
import com.bancrabs.villaticket.models.dtos.save.SaveLocaleDTO;
import com.bancrabs.villaticket.models.entities.QR;
import com.bancrabs.villaticket.models.entities.Ticket;
import com.bancrabs.villaticket.models.entities.Transfer;
import com.bancrabs.villaticket.services.LocaleService;
import com.bancrabs.villaticket.services.QRService;
import com.bancrabs.villaticket.services.TicketService;
import com.bancrabs.villaticket.services.TransferService;

import jakarta.validation.Valid;
import net.glxn.qrgen.javase.QRCode;

@RestController
@RequestMapping("/api/ticketaux")
public class TicketAuxController {
    
    @Autowired
    private LocaleService localeService;

    @Autowired
    private QRService qrService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TransferService transferService;

    @GetMapping("/locale")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getAllLocales(){
        return new ResponseEntity<>(localeService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/locale")
    public ResponseEntity<?> createLocale(@ModelAttribute @Valid SaveLocaleDTO data, BindingResult result){
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            if(localeService.save(data)){
                return new ResponseEntity<>("Created", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/qr")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getAllQR(){
        return new ResponseEntity<>(qrService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/qr/ticket")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> createTicketQR(@ModelAttribute("ticketId") UUID ticketId){
        try{
            Ticket ticket = ticketService.findById(ticketId);
            if(ticket == null){
                return new ResponseEntity<>("Ticket not found", HttpStatus.NOT_FOUND);
            }
            QR newQR = qrService.save(passwordEncoder.encode(ticketId.toString() + Long.toString(System.currentTimeMillis())));
            if(newQR == null){
                return new ResponseEntity<>("QR not created", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new QRResponseDTO(newQR.getCode()), HttpStatus.CREATED);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/qr/transfer")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> createTransferQR(@ModelAttribute("transferId") UUID transferId){
        try{
            Transfer transfer = transferService.findById(transferId);
            if(transfer == null){
                return new ResponseEntity<>("Transfer not found", HttpStatus.NOT_FOUND);
            }
            QR newQR = qrService.save(QRCode.from(transferId.toString()).toString());
            if(newQR == null){
                return new ResponseEntity<>("QR not created", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new QRResponseDTO(newQR.getCode()), HttpStatus.CREATED);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
