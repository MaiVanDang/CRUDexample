package com.boostmytool.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private static final String UPLOAD_DIR = "public/images/";

    public String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);

        // Kiểm tra xem thư mục tồn tại chưa
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        Date updatedAt = new Date();
        String storageFileName = updatedAt.getTime() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(storageFileName);
        
        // Nếu file chưa tồn tại, tiến hành lưu file
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return storageFileName;
    }
    
    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException ex) {
            System.out.println("Exception deleting file: " + ex.getMessage());
        }
    }
}

