package com.example.shop.controller;

import com.example.shop.dto.ProductDTO;
import com.example.shop.model.Product;
import com.example.shop.service.OrderService;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import com.example.shop.service.FirebaseStorageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final FirebaseStorageService storageService;
    
    public AdminController(ProductService productService, OrderService orderService,
                          UserService userService, FirebaseStorageService storageService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
        this.storageService = storageService;
    }
    
    @GetMapping
    public String adminDashboard() {
        return "redirect:/admin/products";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products";
    }
    
    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("isEdit", false);
        return "admin/product-form";
    }
    
    @PostMapping("/products")
    public String createOrUpdateProduct(@Valid @ModelAttribute ProductDTO productDTO,
                                        BindingResult result,
                                        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/product-form";
        }
        
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadedUrl = storageService.uploadFile(imageFile, "products");
                productDTO.setImageUrl(uploadedUrl);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas uploadu pliku: " + e.getMessage());
            return "redirect:/admin/products";
        }

        if (productDTO.getId() != null && !productDTO.getId().isEmpty()) {
            productService.findById(productDTO.getId())
                .ifPresentOrElse(product -> {
                    product.setName(productDTO.getName());
                    product.setDescription(productDTO.getDescription());
                    product.setPrice(productDTO.getPrice());
                    product.setCategory(productDTO.getCategory());
                    product.setStockQuantity(productDTO.getStockQuantity());
                    product.setImageUrl(productDTO.getImageUrl());
                    productService.save(product);
                    redirectAttributes.addFlashAttribute("success", "Produkt zaktualizowany pomyślnie");
                }, () -> redirectAttributes.addFlashAttribute("error", "Nie znaleziono produktu"));
        } else {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setCategory(productDTO.getCategory());
            product.setStockQuantity(productDTO.getStockQuantity());
            product.setImageUrl(productDTO.getImageUrl());
            productService.save(product);
            redirectAttributes.addFlashAttribute("success", "Produkt dodany pomyślnie");
        }
        
        return "redirect:/admin/products";
    }
    
    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable String id, Model model) {
        return productService.findById(id)
            .map(product -> {
                ProductDTO dto = new ProductDTO();
                dto.setId(product.getId());
                dto.setName(product.getName());
                dto.setDescription(product.getDescription());
                dto.setPrice(product.getPrice());
                dto.setCategory(product.getCategory());
                dto.setStockQuantity(product.getStockQuantity());
                dto.setImageUrl(product.getImageUrl());
                model.addAttribute("productDTO", dto);
                model.addAttribute("isEdit", true);
                return "admin/product-form";
            })
            .orElse("redirect:/admin/products");
    }
    
    
    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable String id,
                               @Valid @ModelAttribute ProductDTO productDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        return "redirect:/admin/products";
    }
    
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable String id, 
                               RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Produkt usunięty");
        return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin/orders";
    }
    
    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable String id,
                                   @RequestParam String status,
                                   RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Status zamówienia zmieniony");
        return "redirect:/admin/orders";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }
    
    @PostMapping("/users/{uid}/role")
    public String updateUserRole(@PathVariable String uid,
                                @RequestParam String role,
                                RedirectAttributes redirectAttributes) {
        userService.updateUserRole(uid, role);
        redirectAttributes.addFlashAttribute("success", "Rola użytkownika zmieniona");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{uid}/delete")
    public String deleteUser(@PathVariable String uid,
                             RedirectAttributes redirectAttributes) {
        userService.deleteUser(uid);
        redirectAttributes.addFlashAttribute("success", "Użytkownik usunięty");
        return "redirect:/admin/users";
    }
}
