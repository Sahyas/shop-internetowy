package com.example.shop.repository;

import com.example.shop.model.User;
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
public class UserRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private static final String COLLECTION_NAME = "users";
    
    private final Firestore firestore;
    
    public UserRepository(Firestore firestore) {
        this.firestore = firestore;
    }
    
    public Optional<User> findByUid(String uid) {
        if (firestore == null) return Optional.empty();
        
        try {
            DocumentSnapshot doc = firestore.collection(COLLECTION_NAME)
                .document(uid).get().get();
                
            if (doc.exists()) {
                return Optional.of(doc.toObject(User.class));
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas pobierania użytkownika: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    public Optional<User> findByEmail(String email) {
        if (firestore == null) return Optional.empty();
        
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("email", email).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            if (!documents.isEmpty()) {
                return Optional.of(documents.get(0).toObject(User.class));
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas wyszukiwania użytkownika po email: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    public User save(User user) {
        if (firestore == null) return user;
        
        try {
            firestore.collection(COLLECTION_NAME)
                .document(user.getUid())
                .set(user).get();
            return user;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas zapisywania użytkownika: " + e.getMessage());
            return user;
        }
    }
    
    public List<User> findAll() {
        if (firestore == null) return new ArrayList<>();
        
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                try {
                    users.add(doc.toObject(User.class));
                } catch (Exception e) {
                    logger.warn("Nie można deserializować użytkownika: " + e.getMessage());
                }
            }
            return users;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Błąd podczas pobierania użytkowników: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public void deleteAll() {
        if (firestore == null) return;
        
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
            for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
                doc.getReference().delete().get();
            }
            logger.info("Wyczyszczono kolekcję użytkowników");
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Błąd podczas czyszczenia kolekcji: " + e.getMessage());
        }
    }
}
