package com.bancrabs.villaticket.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.bancrabs.villaticket.models.dtos.save.RegisterOrderDTO;
import com.bancrabs.villaticket.models.entities.Order;

public interface OrderService {
    UUID save(RegisterOrderDTO data) throws Exception;

    Page<Order> findAll(int page, int size);
    Page<Order> findByUserId(UUID userId, int page, int size);
    Page<Order> findByPurchaseDate(Date date, int page, int size);
    Page<Order> findByPurchaseMethod(String method, int page, int size);
    Order findById(UUID id);
}
