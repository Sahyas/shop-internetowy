package com.example.shop.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private String id;
    private String userId;
    private Map<String, Integer> items;
    private long updatedAt;
    
    public Cart() {
        this.items = new HashMap<>();
        this.updatedAt = System.currentTimeMillis();
    }
    
    public Cart(String userId) {
        this();
        this.userId = userId;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public Map<String, Integer> getItems() { return items; }
    public void setItems(Map<String, Integer> items) { this.items = items; }
    
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
