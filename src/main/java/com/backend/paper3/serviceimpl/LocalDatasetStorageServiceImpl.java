package com.backend.paper3.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.paper3.service.LocalDatasetStorageService;

@Service
public class LocalDatasetStorageServiceImpl implements LocalDatasetStorageService {

    @Value("${file.storage}")
    private String storagePath;

    @Override
    public String storeFile(MultipartFile file) {

        try {

            File dir = new File(storagePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(storagePath, fileName);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return path.toString();

        } catch (IOException e) {
            throw new RuntimeException("File storage failed: " + e.getMessage());
        }
    }
}