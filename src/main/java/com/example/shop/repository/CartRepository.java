package com.example.shop.repository;

import com.example.shop.model.Cart;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class CartRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(CartRepository.class);
    private static final String COLLECTION_NAME = "carts";
    
    private final Firestore firestore;
    
    public CartRepository(Firestore firestore) {
        this.firestore = firestore;
    }
    
    public Cart save(Cart cart) {
        if (firestore == null) return cart;
        
        try {
            cart.setUpdatedAt(System.currentTimeMillis());
            
            if (cart.getId() == null || cart.getId().isEmpty()) {
                DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
                cart.setId(docRef.getId());
                docRef.set(cart).get();
            } else {
                firestore.collection(COLLECTION_NAME)
                    .document(cart.getId())
                    .set(cart).get();
            }
            return cart;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas zapisywania koszyka: " + e.getMessage());
            return cart;
        }
    }
    
    public Optional<Cart> findByUserId(String userId) {
        if (firestore == null) return Optional.empty();
        
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .limit(1)
                .get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            if (!documents.isEmpty()) {
                QueryDocumentSnapshot doc = documents.get(0);
                Cart cart = doc.toObject(Cart.class);
                cart.setId(doc.getId());
                return Optional.of(cart);
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas pobierania koszyka: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    public void deleteByUserId(String userId) {
        if (firestore == null) return;
        
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            for (QueryDocumentSnapshot doc : documents) {
                doc.getReference().delete().get();
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas usuwania koszyka: " + e.getMessage());
        }
    }
}
