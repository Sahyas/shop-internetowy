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
        boolean hasCategory = category != null && !category.isEmpty();
        model.addAttribute("products", hasCategory ? productService.findByCategory(category) : productService.findAll());
        if (hasCategory) {
            model.addAttribute("selectedCategory", category);
        }
        return "index";
    }
    
    @GetMapping("/products")
    public String products(@RequestParam(required = false) String category, Model model) {
        return index(category, model);
    }
}
