package com.example.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDTO {
    
    @NotEmpty(message = "Email jest wymagany")
    @Email(message = "Nieprawidłowy format email")
    private String email;
    
    @NotEmpty(message = "Hasło jest wymagane")
    @Size(min = 6, message = "Hasło musi mieć minimum 6 znaków")
    private String password;
    
    @NotEmpty(message = "Potwierdzenie hasła jest wymagane")
    private String confirmPassword;
    
    public RegisterDTO() {}
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { 
        this.confirmPassword = confirmPassword; 
    }
}
