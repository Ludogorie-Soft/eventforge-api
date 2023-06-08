package com.eventforge.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadImageToFileSystem(MultipartFile file) throws IOException;
    String getImageAddressFromFileSystem(String fileName) throws IOException;
    ResponseEntity<Resource> downloadImageFromFileSystem(String fileName) throws IOException;
    void deleteImageFromFileSystem(String fileName);


}
