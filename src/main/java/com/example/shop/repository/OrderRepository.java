package com.example.shop.repository;

import com.example.shop.model.Order;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class OrderRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);
    private static final String COLLECTION_NAME = "orders";
    
    private final Firestore firestore;
    
    public OrderRepository(Firestore firestore) {
        this.firestore = firestore;
    }
    
    public Order save(Order order) {
        if (firestore == null) return order;
        
        try {
            if (order.getId() == null || order.getId().isEmpty()) {
                DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
                order.setId(docRef.getId());
                docRef.set(order).get();
            } else {
                firestore.collection(COLLECTION_NAME)
                    .document(order.getId())
                    .set(order).get();
            }
            return order;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas zapisywania zamówienia: " + e.getMessage());
            return order;
        }
    }
    
    public Optional<Order> findById(String id) {
        if (firestore == null) return Optional.empty();
        
        try {
            DocumentSnapshot doc = firestore.collection(COLLECTION_NAME)
                .document(id).get().get();
                
            if (doc.exists()) {
                Order order = doc.toObject(Order.class);
                order.setId(doc.getId());
                return Optional.of(order);
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas pobierania zamówienia: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    public List<Order> findByUserId(String userId) {
        if (firestore == null) return new ArrayList<>();
        
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            List<Order> orders = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                Order order = doc.toObject(Order.class);
                order.setId(doc.getId());
                orders.add(order);
            }
            // Sortowanie w pamięci
            orders.sort((o1, o2) -> Long.compare(o2.getCreatedAt(), o1.getCreatedAt()));
            return orders;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas pobierania zamówień użytkownika: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Order> findAll() {
        if (firestore == null) return new ArrayList<>();
        
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            List<Order> orders = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                Order order = doc.toObject(Order.class);
                order.setId(doc.getId());
                orders.add(order);
            }
            // Sortowanie w pamięci
            orders.sort((o1, o2) -> Long.compare(o2.getCreatedAt(), o1.getCreatedAt()));
            return orders;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas pobierania wszystkich zamówień: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public void clearCollection() {
        if (firestore == null) return;
        
        try {
            logger.info("Czyszczenie kolekcji zamówień...");
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            for (QueryDocumentSnapshot doc : documents) {
                doc.getReference().delete().get();
            }
            logger.info("Kolekcja zamówień wyczyszczona: {} dokumentów usuniętych", documents.size());
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas czyszczenia kolekcji zamówień: " + e.getMessage());
        }
    }
}
