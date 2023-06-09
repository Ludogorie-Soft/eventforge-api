package com.eventforge.service.Impl;

import com.eventforge.exception.GlobalException;
import com.eventforge.model.Image;
import com.eventforge.repository.ImageRepository;
import com.eventforge.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    private static final String FOLDER_PATH = "static/main/resources/static/images/";
    private final ImageRepository imageRepository;

    @Override
    public String uploadImageToFileSystem(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (isFileNameExists(fileName)) {
            throw new GlobalException("Файл с това име вече съществува.");
        }

        Path filePath = Paths.get(FOLDER_PATH, fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new GlobalException("Грешка със запазването на файла.");
        }

        imageRepository.save(Image.builder()
                .url(fileName)
                .uploadAt(LocalDateTime.now())
                .type(file.getContentType())
                .build());

        return "Файлът е запазен успешно! Пътят до файла: " + filePath;
    }

    private boolean isFileNameExists(String fileName) {
        Optional<Image> imageOptional = imageRepository.findImageByName(fileName);
        return imageOptional.isPresent();
    }

    public String getImageAddressFromFileSystem(String fileName) throws IOException {
        File file = new File(FOLDER_PATH, fileName);
        Resource resource = new UrlResource(file.toURI().toURL());

        if (resource.exists()) {
            String absolutePath = resource.getURI().getPath();
            return absolutePath.substring(absolutePath.indexOf("static/main/resources"));
        } else {
            throw new GlobalException("Файл с това име вече съществува в базата данни.");
        }
    }

    public ResponseEntity<Resource> downloadImageFromFileSystem(String fileName) throws IOException {
        File file = new File(FOLDER_PATH, fileName);
        Resource resource = new UrlResource(file.toURI());

        if (resource.exists()) {
            String fileExtension = getFileExtension(fileName);
            MediaType mediaType = determineMediaType(fileExtension);

            if (mediaType != null) {
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

    MediaType determineMediaType(String fileExtension) {
        if (fileExtension != null) {
            if (fileExtension.equals("png")) {
                return MediaType.IMAGE_PNG;
            } else if (fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {
                return MediaType.IMAGE_JPEG;
            }
            throw new IllegalStateException("Unexpected value: " + fileExtension);
        }
        return null;
    }

    @Override
    public void deleteImageFromFileSystem(String fileName) {
        Optional<Image> imageOptional = getImageByName(fileName);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            deleteImageFile(fileName);
            imageRepository.delete(image);
        } else {
            throw new GlobalException("Файлът не може да бъде открит");
        }
    }

    protected Optional<Image> getImageByName(String fileName) {
        return imageRepository.findImageByName(fileName);
    }

    protected void deleteImageFile(String fileName) {
        String filePath = FOLDER_PATH + fileName;
        Path fileToDelete = Paths.get(filePath);
        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            throw new GlobalException("Грешка с изтриването на файла.");
        }
    }
}
