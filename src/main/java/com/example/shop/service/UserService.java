package com.example.shop.service;

import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @PostConstruct
    public void initTestUsers() {
        // konta testowe
        try {
            createTestUser("admin@example.com", "admin123", "ROLE_ADMIN");
            createTestUser("user@example.com", "user123", "ROLE_USER");
        } catch (Exception e) {
            logger.warn("Błąd przy tworzeniu kont testowych: " + e.getMessage());
        }
    }
    
    private void createTestUser(String email, String password, String role) {
        try {
            findByEmail(email).ifPresentOrElse(
                u -> logger.info("Konto testowe już istnieje: " + email),
                () -> {
                    User user = new User();
                    user.setUid("test-" + email.replace("@", "-").replace(".", "-"));
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setRole(role);
                    userRepository.save(user);
                    logger.info("Utworzono konto testowe: " + email + " z zaszyfrowanym hasłem");
                }
            );
        } catch (Exception e) {
            logger.error("Błąd podczas tworzenia użytkownika testowego: " + e.getMessage(), e);
        }
    }
    
    public User registerUser(String email, String password) throws FirebaseAuthException {
        // rejestracja w Firebase Auth
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
            .setEmail(email)
            .setPassword(password)
            .setEmailVerified(false);
            
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        
        // zapis do Firestore z zaszyfrowanym hasłem
        User user = new User(userRecord.getUid(), email, "ROLE_USER");
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
    
    public Optional<User> findByUid(String uid) {
        return userRepository.findByUid(uid);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public FirebaseToken verifyIdToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public User updateUserRole(String uid, String newRole) {
        return userRepository.findByUid(uid)
            .map(user -> {
                user.setRole(newRole);
                return userRepository.save(user);
            })
            .orElse(null);
    }

    public void deleteUser(String uid) {
        userRepository.deleteByUid(uid);
    }
}
