package com.example.shop.controller;

import com.example.shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    private final ProductService productService;
    
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String category, Model model) {
        if (category != null && !category.isEmpty()) {
            model.addAttribute("products", productService.findByCategory(category));
            model.addAttribute("selectedCategory", category);
        } else {
            model.addAttribute("products", productService.findAll());
        }
        return "index";
    }
    
    @GetMapping("/products")
    public String products(@RequestParam(required = false) String category, Model model) {
        return index(category, model);
    }
}
