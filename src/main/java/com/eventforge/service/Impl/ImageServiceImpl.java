package com.eventforge.service.Impl;

import com.eventforge.constants.ImageType;
import com.eventforge.exception.ImageException;
import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.repository.ImageRepository;
import com.eventforge.service.ImageService;
import jakarta.annotation.Nullable;
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
    private static final String FOLDER_PATH = "src/main/resources/static/images/";
    private final ImageRepository imageRepository;

    @Override
    public void uploadImageToFileSystem(MultipartFile file , ImageType imageType , Organisation organisation , Event event) {
        String fileName = file.getOriginalFilename();
        if (doesFileNameExists(fileName)) {
            throw new ImageException("Файл с това име вече съществува.");
        }

        String sanitizedFileName = null;
        if (fileName != null) {
            sanitizedFileName = sanitizeFileName(fileName);
        }

        Path uploadDirectory = Paths.get(FOLDER_PATH);
        assert sanitizedFileName != null;
        Path filePath = uploadDirectory.resolve(sanitizedFileName);

        try {
            if (!Files.exists(uploadDirectory)) {
                Files.createDirectories(uploadDirectory);
            }

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ImageException("Грешка със запазването на файла.");
        }

        if(organisation!=null && (imageType!=null && imageType.equals(ImageType.LOGO))){
            imageRepository.save(Image.builder()
                    .url(sanitizedFileName)
                    .uploadAt(LocalDateTime.now())
                    .type(imageType)
                            .organisation(organisation)
                    .build());
        }
        if(organisation != null && (imageType!=null && imageType.equals(ImageType.COVER))){
            imageRepository.save(Image.builder()
                    .url(sanitizedFileName)
                    .uploadAt(LocalDateTime.now())
                    .type(imageType)
                    .organisation(organisation)
                    .build());
        }

        if(event!=null && (imageType!=null && imageType.equals(ImageType.EVENT_PICTURE))){
            imageRepository.save(Image.builder()
                    .url(sanitizedFileName)
                    .uploadAt(LocalDateTime.now())
                    .type(imageType)
                    .event(event)
                    .build());
        }


    }

    private String sanitizeFileName(String fileName) {
        String allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.";

        String sanitizedFileName = fileName.replace("..", "");

        StringBuilder sb = new StringBuilder();
        for (char c : sanitizedFileName.toCharArray()) {
            if (allowedCharacters.indexOf(c) != -1) {
                sb.append(c);
            }
        }
        sanitizedFileName = sb.toString();

        int maxFileNameLength = 255;
        if (sanitizedFileName.length() > maxFileNameLength) {
            throw new ImageException("Името на файла надвишава максималната дължина.");
        }

        return sanitizedFileName;
    }

    private boolean doesFileNameExists(String fileName) {
        Optional<Image> imageOptional = imageRepository.findImageByName(fileName);
        return imageOptional.isPresent();
    }

    public String getImageAddressFromFileSystem(String fileName) throws IOException {
        File file = getFileFromPath(fileName);
        Resource resource = new UrlResource(file.toURI());

        if (resource.exists()) {
            String absolutePath = resource.getURI().getPath();
            return absolutePath.substring(absolutePath.indexOf("src/main/resources"));
        } else {
            throw new ImageException("Файл с това име вече съществува в базата данни.");
        }
    }

    private File getFileFromPath(String fileName) {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            throw new ImageException("Моля, проверете пътят до файла дали е верен!");
        }
        File file = new File(folder, fileName);
        if (!file.exists()) {
            throw new ImageException("Файл с такова име не съществува");
        }
        return file;
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

    @Nullable
    String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }
    @Nullable
    MediaType determineMediaType(String fileExtension) {
        if (fileExtension != null) {
            if (fileExtension.equals("png")) {
                return MediaType.IMAGE_PNG;
            } else if (fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {
                return MediaType.IMAGE_JPEG;
            }
            throw new ImageException("Грешно разширение на файла: " + fileExtension);
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
            throw new ImageException("Файлът не може да бъде открит");
        }
    }

    Optional<Image> getImageByName(String fileName) {
        return imageRepository.findImageByName(fileName);
    }

    void deleteImageFile(String fileName) {
        String filePath = FOLDER_PATH + fileName;
        Path fileToDelete = Paths.get(filePath);
        deleteFile(fileToDelete);
    }

    void deleteFile(Path fileToDelete) {
        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            throw new ImageException("Грешка с изтриването на файла.");
        }
    }
}
