package com.example.shop.service;

import com.example.shop.model.Cart;
import com.example.shop.model.Product;
import com.example.shop.repository.CartRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    
    private final CartRepository cartRepository;
    private final UserService userService;
    
    public CartService(CartRepository cartRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
    }
    
    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String email = userDetails.getUsername();
            var userOpt = userService.findByEmail(email);
            if (userOpt.isPresent()) {
                return userOpt.get().getUid();
            }
        }
        return "guest";
    }
    
    private Cart getOrCreateCart() {
        String userId = getCurrentUserId();
        return cartRepository.findByUserId(userId)
            .orElseGet(() -> new Cart(userId));
    }
    
    public void addToCart(String productId, int quantity) {
        Cart cart = getOrCreateCart();
        Map<String, Integer> items = cart.getItems();
        items.merge(productId, quantity, Integer::sum);
        cart.setItems(items);
        cartRepository.save(cart);
    }
    
    public void updateQuantity(String productId, int quantity) {
        Cart cart = getOrCreateCart();
        Map<String, Integer> items = cart.getItems();
        
        if (quantity <= 0) {
            items.remove(productId);
        } else {
            items.put(productId, quantity);
        }
        
        cart.setItems(items);
        cartRepository.save(cart);
    }
    
    public void removeFromCart(String productId) {
        Cart cart = getOrCreateCart();
        Map<String, Integer> items = cart.getItems();
        items.remove(productId);
        cart.setItems(items);
        cartRepository.save(cart);
    }
    
    public Map<String, Integer> getCartItems() {
        Cart cart = getOrCreateCart();
        return new HashMap<>(cart.getItems());
    }
    
    public int getItemCount() {
        Cart cart = getOrCreateCart();
        return cart.getItems().values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public void clear() {
        String userId = getCurrentUserId();
        cartRepository.deleteByUserId(userId);
    }
    
    public double calculateTotal(ProductService productService) {
        double total = 0.0;
        Map<String, Integer> items = getCartItems();
        
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            var productOpt = productService.findById(entry.getKey());
            if (productOpt.isPresent()) {
                total += productOpt.get().getPrice() * entry.getValue();
            }
        }
        return total;
    }
}
