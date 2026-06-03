package com.backend.paper3.serviceimpl;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.backend.paper3.exception.ApiException;
import com.backend.paper3.service.LocalDatasetStorageService;

@Service
public class LocalDatasetStorageServiceImpl
        implements LocalDatasetStorageService {

    @Value("${file.storage:uploads/datasets}")
    private String storagePath;

    @Override
    public String storeFile(MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {
                throw new ApiException("Uploaded file is empty");
            }

            Path storageDir = Paths.get(storagePath)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(storageDir);

            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null || originalFileName.isBlank()) {
                throw new ApiException("Invalid file name");
            }

            String cleanFileName = StringUtils.cleanPath(originalFileName);

            if (cleanFileName.contains("..")) {
                throw new ApiException("Invalid file path in file name");
            }

            String uniqueFileName =
                    UUID.randomUUID() + "_" + cleanFileName;

            Path targetPath = storageDir
                    .resolve(uniqueFileName)
                    .normalize();

            if (!targetPath.startsWith(storageDir)) {
                throw new ApiException("Invalid storage path");
            }

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return targetPath.toString();

        } catch (IOException e) {

            throw new ApiException(
                    "File storage failed : " + e.getMessage()
            );
        }
    }
}