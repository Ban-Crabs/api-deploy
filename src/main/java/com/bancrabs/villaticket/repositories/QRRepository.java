package com.bancrabs.villaticket.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.QR;

public interface QRRepository extends JpaRepository<QR, UUID>{
    QR findByCode(String code);
}
