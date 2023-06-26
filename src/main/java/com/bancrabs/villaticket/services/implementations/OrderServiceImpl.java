package com.bancrabs.villaticket.services.implementations;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.RegisterOrderDTO;
import com.bancrabs.villaticket.models.entities.Order;
import com.bancrabs.villaticket.models.entities.User;
import com.bancrabs.villaticket.repositories.OrderRepository;
import com.bancrabs.villaticket.services.OrderService;
import com.bancrabs.villaticket.services.UserService;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;


    @Override
    public UUID save(RegisterOrderDTO data) throws Exception {
        try{
            User user = userService.findById(data.getUserId());
            if(user == null){
                throw new Exception("User not found");
            }
            Order order = orderRepository.save(new Order(data.getPurchaseDate(), data.getPurchaseMethod(), user));
            return order.getId();
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Page<Order> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<Order> findByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Order> findByPurchaseDate(Date date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByPurchaseDate(date, pageable);
    }

    @Override
    public Page<Order> findByPurchaseMethod(String method, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByPurchaseMethod(method, pageable);
    }

    @Override
    public Order findById(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }
    
}
