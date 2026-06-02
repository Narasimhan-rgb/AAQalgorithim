package com.backend.paper3.service;

import org.springframework.web.multipart.MultipartFile;

public interface LocalDatasetStorageService {

    String storeFile(MultipartFile file);

}