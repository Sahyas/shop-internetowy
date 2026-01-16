package com.example.shop.dto;

import jakarta.validation.constraints.NotEmpty;

public class CheckoutDTO {
    
    @NotEmpty(message = "Ulica jest wymagana")
    private String street;
    
    @NotEmpty(message = "Miasto jest wymagane")
    private String city;
    
    @NotEmpty(message = "Kod pocztowy jest wymagany")
    private String postalCode;
    
    @NotEmpty(message = "Numer telefonu jest wymagany")
    private String phone;
    
    public CheckoutDTO() {}
    
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
