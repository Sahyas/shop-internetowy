package com.example.shop.controller;

import com.example.shop.dto.CheckoutDTO;
import com.example.shop.model.Order;
import com.example.shop.model.Product;
import com.example.shop.service.CartService;
import com.example.shop.service.OrderService;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    
    public CartController(CartService cartService, ProductService productService, 
                         OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }
    
    @GetMapping
    public String viewCart(Model model) {
        Map<String, Integer> cartItems = cartService.getCartItems();
        List<CartItemView> items = new ArrayList<>();
        double total = 0.0;
        
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            var productOpt = productService.findById(entry.getKey());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                int quantity = entry.getValue();
                double subtotal = product.getPrice() * quantity;
                items.add(new CartItemView(product, quantity, subtotal));
                total += subtotal;
            }
        }
        
        model.addAttribute("cartItems", items);
        model.addAttribute("total", total);
        model.addAttribute("itemCount", cartService.getItemCount());
        return "cart";
    }
    
    @PostMapping("/update")
    public String updateCart(@RequestParam String productId, 
                            @RequestParam int quantity,
                            RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(productId, quantity);
        redirectAttributes.addFlashAttribute("success", "Koszyk zaktualizowany");
        return "redirect:/cart";
    }
    
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam String productId,
                                RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(productId);
        redirectAttributes.addFlashAttribute("success", "Produkt usunięty z koszyka");
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        if (cartService.getItemCount() == 0) {
            return "redirect:/cart";
        }
        
        model.addAttribute("checkoutDTO", new CheckoutDTO());
        model.addAttribute("total", cartService.calculateTotal(productService));
        return "checkout";
    }
    
    @PostMapping("/checkout")
    public String processCheckout(@Valid @ModelAttribute CheckoutDTO checkoutDTO,
                                 BindingResult result,
                                 Authentication authentication,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("total", cartService.calculateTotal(productService));
            return "checkout";
        }
        
        // pobierz userId z zalogowanego użytkownika
        String userId = "guest";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            // pobierz UID użytkownika
            var userOpt = userService.findByEmail(email);
            if (userOpt.isPresent()) {
                userId = userOpt.get().getUid();
            }
        }
        
        // tworzenie zamówienia
        Order order = new Order();
        order.setUserId(userId);
        
        Order.ShippingAddress address = new Order.ShippingAddress();
        address.setStreet(checkoutDTO.getStreet());
        address.setCity(checkoutDTO.getCity());
        address.setPostalCode(checkoutDTO.getPostalCode());
        address.setPhone(checkoutDTO.getPhone());
        order.setShippingAddress(address);
        
        // dodaj produkty
        Map<String, Integer> cartItems = cartService.getCartItems();
        List<Order.OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;
        
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            var productOpt = productService.findById(entry.getKey());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                Order.OrderItem item = new Order.OrderItem(
                    product.getId(),
                    product.getName(),
                    entry.getValue(),
                    product.getPrice()
                );
                orderItems.add(item);
                totalAmount += product.getPrice() * entry.getValue();
            }
        }
        
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setStatus("ZŁOŻONE");
        
        Order savedOrder = orderService.createOrder(order);
        cartService.clear();
        
        redirectAttributes.addFlashAttribute("orderId", savedOrder.getId());
        return "redirect:/cart/confirmation";
    }
    
    @GetMapping("/confirmation")
    public String confirmation() {
        return "order-confirmation";
    }
    
    // klasa pomocnicza do wyświetlania
    public static class CartItemView {
        private Product product;
        private int quantity;
        private double subtotal;
        
        public CartItemView(Product product, int quantity, double subtotal) {
            this.product = product;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }
        
        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public double getSubtotal() { return subtotal; }
    }
}
