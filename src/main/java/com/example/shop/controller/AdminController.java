package com.example.shop.controller;

import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}
