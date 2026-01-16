package com.example.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
        // TODO: Initialize Firebase Admin SDK here if service account is provided.
        // Example:
        // GoogleCredentials cred = GoogleCredentials.fromStream(new FileInputStream("serviceAccountKey.json"));
        // FirebaseOptions options = FirebaseOptions.builder().setCredentials(cred).build();
        // FirebaseApp.initializeApp(options);
    }
}
