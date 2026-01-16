package com.example.shop.model;

public class User {
    private String uid;  // Firebase UID
    private String email;
    private String password;  // Zaszyfrowane hasło
    private String role;  // ROLE_USER lub ROLE_ADMIN
    private long createdAt;  // timestamp zamiast LocalDateTime
    
    public User() {
        this.createdAt = System.currentTimeMillis();
    }
    
    public User(String uid, String email, String role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
        this.createdAt = System.currentTimeMillis();
    }
    
    // gettery i settery
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
