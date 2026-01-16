package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
    
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }
    
    public List<Order> findByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    
    public Order updateOrderStatus(String orderId, String newStatus) {
        return findById(orderId)
            .map(order -> {
                order.setStatus(newStatus);
                return orderRepository.save(order);
            })
            .orElse(null);
    }
}
