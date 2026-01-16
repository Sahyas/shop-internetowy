package com.example.shop.repository;

import com.example.shop.model.Product;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);
    private static final String COLLECTION_NAME = "products";
    
    private final Firestore firestore;
    
    public ProductRepository(Firestore firestore) {
        this.firestore = firestore;
    }
    
    public List<Product> findAll() {
        if (firestore == null) return new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> documents = firestore.collection(COLLECTION_NAME).get().get().getDocuments();
            return documents.stream()
                .map(doc -> {
                    Product product = doc.toObject(Product.class);
                    product.setId(doc.getId());
                    return product;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Błąd podczas pobierania produktów", e);
            return new ArrayList<>();
        } catch (ExecutionException e) {
            logger.error("Błąd podczas pobierania produktów", e);
            return new ArrayList<>();
        }
    }
    
    public Optional<Product> findById(String id) {
        if (firestore == null) return Optional.empty();

        try {
            DocumentSnapshot doc = firestore.collection(COLLECTION_NAME)
                .document(id).get().get();

            return doc.exists()
                ? Optional.ofNullable(doc.toObject(Product.class))
                    .map(product -> {
                        product.setId(doc.getId());
                        return product;
                    })
                : Optional.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Błąd podczas pobierania produktu", e);
            return Optional.empty();
        } catch (ExecutionException e) {
            logger.error("Błąd podczas pobierania produktu", e);
            return Optional.empty();
        }
    }
    
    public Product save(Product product) {
        if (firestore == null) return product;
        
        try {
            if (product.getId() == null || product.getId().isEmpty()) {
                // nowy produkt
                DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
                product.setId(docRef.getId());
                docRef.set(product).get();
            } else {
                // aktualizacja istniejącego
                firestore.collection(COLLECTION_NAME)
                    .document(product.getId())
                    .set(product).get();
            }
            return product;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas zapisywania produktu: " + e.getMessage());
            return product;
        }
    }
    
    public void delete(String id) {
        if (firestore == null) return;
        
        try {
            firestore.collection(COLLECTION_NAME).document(id).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas usuwania produktu: " + e.getMessage());
        }
    }
    
    public List<Product> findByCategory(String category) {
        if (firestore == null) return new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> documents = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("category", category).get().get().getDocuments();

            return documents.stream()
                .map(doc -> {
                    Product product = doc.toObject(Product.class);
                    product.setId(doc.getId());
                    return product;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Błąd podczas filtrowania produktów", e);
            return new ArrayList<>();
        } catch (ExecutionException e) {
            logger.error("Błąd podczas filtrowania produktów", e);
            return new ArrayList<>();
        }
    }
}
