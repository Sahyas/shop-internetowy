package com.example.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private String userId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status;  // ZŁOŻONE, WYSŁANE, DOSTARCZONO
    private ShippingAddress shippingAddress;
    private long createdAt;
    
    public Order() {
        this.items = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.status = "ZŁOŻONE";
    }
    
    // gettery i settery
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { 
        this.shippingAddress = shippingAddress; 
    }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public static class OrderItem {
        private String productId;
        private String productName;
        private int quantity;
        private double price;
        
        public OrderItem() {}
        
        public OrderItem(String productId, String productName, int quantity, double price) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }
        
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
    
    public static class ShippingAddress {
        private String street;
        private String city;
        private String postalCode;
        private String phone;
        
        public ShippingAddress() {}
        
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
}
