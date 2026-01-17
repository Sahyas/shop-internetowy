package com.example.shop.model;

public class User {
    private String uid;
    private String email;
    private String password;
    private String role;
    private long createdAt;
    
    public User() {
        this.createdAt = System.currentTimeMillis();
    }
    
    public User(String uid, String email, String role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
        this.createdAt = System.currentTimeMillis();
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
