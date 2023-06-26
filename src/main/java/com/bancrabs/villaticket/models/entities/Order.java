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
@Table(name = "order", schema = "public")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "purchase_date", nullable = false)
    private Timestamp purchaseDate;

    @Column(name = "purchase_method", nullable = false)
    private String purchaseMethod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Order(Timestamp purchaseDate, String purchaseMethod, User user) {
        this.purchaseDate = purchaseDate;
        this.purchaseMethod = purchaseMethod;
        this.user = user;
    }
}