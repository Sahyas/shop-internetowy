package com.example.shop.service;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void init() {
        List<Product> existing = productRepository.findAll();
        if (existing.isEmpty()) {
            logger.info("Brak produktów w bazie - pomijam seeding");
            return;
        }
        logger.info("Znaleziono {} istniejących produktów - uzupełniam brakujące obrazki", existing.size());
        backfillMissingImages(existing);
    }

    private void backfillMissingImages(List<Product> products) {
        long updated = products.stream()
            .filter(p -> p.getImageUrl() == null || p.getImageUrl().isBlank())
            .mapToLong(p -> {
                p.setImageUrl(fallbackImageFor(p.getCategory()));
                save(p);
                return 1L;
            })
            .sum();

        if (updated > 0) {
            logger.info("Uzupełniono obrazki dla {} produktów", updated);
        }
    }

    private String fallbackImageFor(String category) {
        if (category == null) return defaultImage();
        String cat = category.toLowerCase();
        if (cat.contains("elektron")) {
            return "/images/products/laptop.svg";
        }
        if (cat.contains("książ") || cat.contains("ksiaz")) {
            return "/images/products/book.svg";
        }
        if (cat.contains("akces")) {
            return "/images/products/keyboard.svg";
        }
        return defaultImage();
    }

    private String defaultImage() {
        return "/images/products/placeholder.svg";
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(String id) {
        productRepository.delete(id);
    }
    
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }
}
