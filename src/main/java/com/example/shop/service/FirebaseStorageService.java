package com.example.shop.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseStorageService.class);

    private final Storage storage;
    private final String bucketName;
    private final Path localUploadDir;

    public FirebaseStorageService(Storage storage,
                                  @Value("${firebase.storage.bucket:}") String storageBucketName,
                                  @Value("${file.upload.dir:uploads}") String uploadDir) throws IOException {
        this.storage = storage;
        this.bucketName = storageBucketName;
        this.localUploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.localUploadDir);
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Jeśli dostępny bucket i Storage — użyj Firebase; w przeciwnym razie lokalny zapis
        if (storage != null && bucketName != null && !bucketName.isBlank()) {
            return uploadToFirebase(file, folder);
        }

        return uploadLocal(file, folder);
    }

    private String uploadToFirebase(MultipartFile file, String folder) throws IOException {
        String safeFolder = (folder == null || folder.isBlank()) ? "uploads" : folder;
        String fileName = safeFolder + "/" + UUID.randomUUID() + "-" + sanitize(file.getOriginalFilename());

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(file.getContentType())
            .build();

        storage.create(blobInfo, file.getBytes());

        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String publicUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" + encoded + "?alt=media";
        logger.info("Plik przesłany do Firebase Storage: {}", publicUrl);
        return publicUrl;
    }

    private String uploadLocal(MultipartFile file, String folder) throws IOException {
        String safeFolder = (folder == null || folder.isBlank()) ? "uploads" : folder;
        Path targetDir = localUploadDir.resolve(safeFolder).normalize();
        Files.createDirectories(targetDir);

        String filename = UUID.randomUUID() + "-" + sanitize(file.getOriginalFilename());
        Path target = targetDir.resolve(filename).normalize();

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // public URL względny dla serwera (obsługiwany przez ResourceHandler w WebConfig)
        String publicUrl = "/uploads/" + safeFolder + "/" + filename;
        logger.info("Plik zapisany lokalnie: {}", target);
        return publicUrl;
    }

    private String sanitize(String name) {
        if (name == null) {
            return "file";
        }
        return name.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
    }
}
