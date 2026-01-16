package com.example.shop.controller;

import com.example.shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable Long id, Model model) {
        productService.findById(id).ifPresent(p -> model.addAttribute("product", p));
        return "product";
    }
}
