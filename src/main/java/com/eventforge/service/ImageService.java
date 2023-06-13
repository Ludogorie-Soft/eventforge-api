package com.eventforge.service;

import com.eventforge.constants.ImageType;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void uploadImageToFileSystem(MultipartFile file, ImageType imageType, Organisation organisation , Event event) throws IOException;
    String getImageAddressFromFileSystem(String fileName) throws IOException;
    ResponseEntity<Resource> downloadImageFromFileSystem(String fileName) throws IOException;
    void deleteImageFromFileSystem(String fileName);


}
