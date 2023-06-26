package com.bancrabs.villaticket.models.entities;

import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "qr", schema = "public")
public class QR {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "creation_time", nullable = false)
    private Timestamp creationTime;

    public QR(String code, Timestamp creationTime) {
        this.code = code;
        this.creationTime = creationTime;
    }

    public QR(String code) {
        this.code = code;
        this.creationTime = new Timestamp(System.currentTimeMillis());
    }
}
