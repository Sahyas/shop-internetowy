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
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        List<CartItemView> items = cartService.getCartItems().entrySet().stream()
            .map(entry -> productService.findById(entry.getKey())
                .map(p -> new CartItemView(p, entry.getValue(), p.getPrice() * entry.getValue())))
            .flatMap(Optional::stream)
            .toList();

        double total = items.stream()
            .mapToDouble(CartItemView::getSubtotal)
            .sum();

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

        String userId = (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails)
            ? userService.findByEmail(userDetails.getUsername())
                .map(u -> u.getUid())
                .orElse("guest")
            : "guest";

        Order order = new Order();
        order.setUserId(userId);
        
        Order.ShippingAddress address = new Order.ShippingAddress();
        address.setStreet(checkoutDTO.getStreet());
        address.setCity(checkoutDTO.getCity());
        address.setPostalCode(checkoutDTO.getPostalCode());
        address.setPhone(checkoutDTO.getPhone());
        order.setShippingAddress(address);

        List<Order.OrderItem> orderItems = cartService.getCartItems().entrySet().stream()
            .map(entry -> productService.findById(entry.getKey())
                .map(product -> new Order.OrderItem(
                    product.getId(),
                    product.getName(),
                    entry.getValue(),
                    product.getPrice()
                )))
            .flatMap(Optional::stream)
            .toList();

        double totalAmount = orderItems.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();

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
