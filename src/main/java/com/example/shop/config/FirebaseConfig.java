package com.example.shop.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);
    
    @Value("${firebase.credentials.path:#{null}}")
    private String credentialsPath;

    @Value("${firebase.storage.bucket:#{null}}")
    private String storageBucket;
    
    @PostConstruct
    public void initialize() {
        try {
            if (credentialsPath != null && !credentialsPath.isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream(credentialsPath);
                
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
                
                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    logger.info("Firebase zainicjalizowany pomyślnie");
                }
            } else {
                logger.warn("Ścieżka do pliku Firebase credentials nie została ustawiona");
                logger.warn("Ustaw firebase.credentials.path w application.properties");
            }
        } catch (IOException e) {
            logger.error("Błąd podczas inicjalizacji Firebase: " + e.getMessage());
        }
    }
    
    @Bean
    public Firestore firestore() {
        if (FirebaseApp.getApps().isEmpty()) {
            logger.warn("Firebase nie został zainicjalizowany - Firestore niedostępny");
            return null;
        }
        return FirestoreClient.getFirestore();
    }

    @Bean
    public Storage storage() {
        if (FirebaseApp.getApps().isEmpty()) {
            logger.warn("Firebase nie został zainicjalizowany - Storage niedostępny");
            return null;
        }
        return StorageOptions.getDefaultInstance().getService();
    }
    
}
