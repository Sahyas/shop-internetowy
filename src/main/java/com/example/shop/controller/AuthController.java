package com.example.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "logout";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
    
    @GetMapping("/register")
    public String register() {
        // do zaimplementowania w przyszłości z Firebase Auth
        return "register";
    }
}
