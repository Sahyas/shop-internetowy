package com.example.shop.controller;

import com.example.shop.model.User;
import com.example.shop.service.OrderService;
import com.example.shop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;
    private final UserService userService;
    
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }
    
    @GetMapping
    public String myOrders(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            var orders = userService.findByEmail(email)
                .map(u -> orderService.findByUserId(u.getUid()))
                .orElse(List.of());
            model.addAttribute("orders", orders);
        }
        return "orders";
    }
    
    @GetMapping("/{id}")
    public String orderDetails(@PathVariable String id, Model model) {
        orderService.findById(id).ifPresent(order -> 
            model.addAttribute("order", order)
        );
        return "order-details";
    }
}
