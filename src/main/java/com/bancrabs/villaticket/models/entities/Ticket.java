package com.bancrabs.villaticket.models.entities;

import java.util.UUID;

import jakarta.persistence.CascadeType;
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
@Table(name = "ticket", schema = "public")
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "tier_id", nullable = false)
    private Tier tier;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "result", nullable = true)
    Boolean result;

    public Ticket(Tier tier) {
        this.tier = tier;
        this.user = null;
        this.result = null;
    }

    public Ticket(Tier tier, User user) {
        this.tier = tier;
        this.user = user;
        this.result = null;
    }

    public Ticket(Tier tier, User user, Boolean result) {
        this.tier = tier;
        this.user = user;
        this.result = result;
    }
}
