package com.example.shop.controller;

import com.example.shop.service.ProductService;
import com.example.shop.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {
    private final ProductService productService;
    private final CartService cartService;

    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable String id, Model model) {
        productService.findById(id).ifPresent(p -> model.addAttribute("product", p));
        return "product";
    }
    
    @PostMapping("/product/{id}/add-to-cart")
    public String addToCart(@PathVariable String id, 
                           @RequestParam(defaultValue = "1") int quantity,
                           RedirectAttributes redirectAttributes) {
        var productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            cartService.addToCart(id, quantity);
            redirectAttributes.addFlashAttribute("success", "Produkt dodany do koszyka");
        } else {
            redirectAttributes.addFlashAttribute("error", "Nie znaleziono produktu");
        }
        return "redirect:/product/" + id;
    }
}
