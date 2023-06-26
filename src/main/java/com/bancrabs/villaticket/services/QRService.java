package com.bancrabs.villaticket.services;

import java.util.List;
import java.util.UUID;

import com.bancrabs.villaticket.models.entities.QR;

public interface QRService {
    QR save(String code) throws Exception;
    Boolean delete(UUID id) throws Exception;

    List<QR> findAll();
    QR findById(UUID id);
    QR findByCode(String code);
}
