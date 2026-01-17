package com.example.shop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {
    
    private String id;
    
    @NotEmpty(message = "Nazwa produktu jest wymagana")
    private String name;
    
    @NotEmpty(message = "Opis produktu jest wymagany")
    private String description;
    
    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0.01", message = "Cena musi być większa niż 0")
    private Double price;
    
    private String category;
    
    @NotNull(message = "Ilość na stanie jest wymagana")
    private Integer stockQuantity;
    
    private String imageUrl;
    
    public ProductDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
