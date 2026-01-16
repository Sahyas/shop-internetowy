package com.example.shop.service;

import com.example.shop.model.Product;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductService {
    private final Map<Long, Product> storage = new ConcurrentHashMap<>();
    private long idSeq = 1;

    @PostConstruct
    public void init() {
        // sample seed data
        save(new Product(null, "Książka Java", "Poradnik programisty Java.", 59.99));
        save(new Product(null, "Mysz USB", "Prosta mysz optyczna.", 39.90));
    }

    public List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Product save(Product p) {
        if (p.getId() == null) p.setId(idSeq++);
        storage.put(p.getId(), p);
        return p;
    }

    public void delete(Long id) {
        storage.remove(id);
    }
}
