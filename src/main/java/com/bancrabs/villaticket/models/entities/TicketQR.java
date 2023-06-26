package com.bancrabs.villaticket.models.entities;

import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ticket_qr", schema = "public")
public class TicketQR {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "exchange_time", nullable = true)
    private Timestamp exchangeTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "qr_id", nullable = false)
    private QR qr;

    public TicketQR(Timestamp exchangeTime, Ticket ticket, QR qr) {
        this.exchangeTime = exchangeTime;
        this.ticket = ticket;
        this.qr = qr;
    }

    public TicketQR(Ticket ticket, QR qr) {
        this.exchangeTime = null;
        this.ticket = ticket;
        this.qr = qr;
    }
}
