package com.example.shop.controller;

import com.example.shop.dto.RegisterDTO;
import com.example.shop.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
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
    public String register(Model model) {
        if (!model.containsAttribute("registerDTO")) {
            model.addAttribute("registerDTO", new RegisterDTO());
        }
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "match", "Hasła muszą być takie same");
        }

        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(registerDTO.getEmail(), registerDTO.getPassword());
            redirectAttributes.addFlashAttribute("success", "Konto utworzone. Zaloguj się.");
            return "redirect:/login";
        } catch (FirebaseAuthException e) {
            result.rejectValue("email", "exists", "Email jest już używany");
            model.addAttribute("registerDTO", registerDTO);
            return "register";
        }
    }
}
