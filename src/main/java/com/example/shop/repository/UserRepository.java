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
import java.util.stream.Collectors;

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

            return doc.exists() ? Optional.ofNullable(doc.toObject(User.class)) : Optional.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Błąd podczas pobierania użytkownika", e);
            return Optional.empty();
        } catch (ExecutionException e) {
            logger.error("Błąd podczas pobierania użytkownika", e);
            return Optional.empty();
        }
    }
    
    public Optional<User> findByEmail(String email) {
        if (firestore == null) return Optional.empty();
        
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("email", email).get();
            return future.get().getDocuments().stream()
                .findFirst()
                .map(doc -> doc.toObject(User.class));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Błąd podczas wyszukiwania użytkownika po email", e);
            return Optional.empty();
        } catch (ExecutionException e) {
            logger.error("Błąd podczas wyszukiwania użytkownika po email", e);
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
            List<QueryDocumentSnapshot> documents = firestore.collection(COLLECTION_NAME).get().get().getDocuments();
            return documents.stream()
                .map(doc -> {
                    try {
                        return Optional.ofNullable(doc.toObject(User.class));
                    } catch (Exception e) {
                        logger.warn("Nie można deserializować użytkownika", e);
                        return Optional.<User>empty();
                    }
                })
                .flatMap(Optional::stream)
                .collect(Collectors.toCollection(ArrayList::new));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Błąd podczas pobierania użytkowników", e);
            return new ArrayList<>();
        } catch (ExecutionException e) {
            logger.error("Błąd podczas pobierania użytkowników", e);
            return new ArrayList<>();
        }
    }
    
    public void deleteAll() {
        if (firestore == null) return;
        
        try {
            firestore.collection(COLLECTION_NAME).get().get().getDocuments().stream()
                .forEach(doc -> {
                    try {
                        doc.getReference().delete().get();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        logger.warn("Przerwano czyszczenie kolekcji użytkowników", ex);
                    } catch (ExecutionException ex) {
                        logger.warn("Błąd podczas czyszczenia dokumentu użytkownika", ex);
                    }
                });
            logger.info("Wyczyszczono kolekcję użytkowników");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Przerwano czyszczenie kolekcji", e);
        } catch (ExecutionException e) {
            logger.warn("Błąd podczas czyszczenia kolekcji", e);
        }
    }

    public void deleteByUid(String uid) {
        if (firestore == null) return;

        try {
            firestore.collection(COLLECTION_NAME).document(uid).delete().get();
            logger.info("Usunięto użytkownika {}", uid);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Przerwano usuwanie użytkownika {}", uid, e);
        } catch (ExecutionException e) {
            logger.warn("Błąd podczas usuwania użytkownika {}", uid, e);
        }
    }
}
