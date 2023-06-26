package com.bancrabs.villaticket.repositories;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>{
    Page<Order> findByUserId(UUID userId, Pageable pageable);
    Page<Order> findByPurchaseDate(Date date, Pageable pageable);
    Page<Order> findByPurchaseMethod(String method, Pageable pageable);
}
