package com.eventforge.controller;

import com.eventforge.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) throws IOException {
        String uploadImage = imageService.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }

    @GetMapping("/get/{fileName}")
    public ResponseEntity<String> getImage(@PathVariable() String fileName) throws IOException {
        String link = imageService.getImageAddressFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK).body(link);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadImage(@PathVariable() String fileName) throws IOException {
        Resource resource = imageService.downloadImageFromFileSystem(fileName).getBody();
        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PostMapping("/delete/{fileName}")
    public ResponseEntity<HttpStatus> deleteImage(@PathVariable("fileName") String fileName) {
        imageService.deleteImageFromFileSystem(fileName);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
