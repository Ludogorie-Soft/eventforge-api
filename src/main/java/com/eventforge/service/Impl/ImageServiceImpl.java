package com.eventforge.service.Impl;

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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    private static final String FOLDER_PATH = "src/main/resources/static/images";
    private final ImageRepository imageRepository;

    @Override
    public String uploadImageToFileSystem(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(FOLDER_PATH + "_" + file.getOriginalFilename());
            file.transferTo(filePath);
            Image image = imageRepository.save(Image.builder().url(fileName).uploadAt(LocalDateTime.now()).type(file.getContentType()).build());

            return imageRepository.findById(image.getId()).map(savedImage -> "Файлът е запазен успешно! Пътят до файла: " + filePath).orElseThrow(() -> new RuntimeException("Грешка при запазване на изображението."));
        } catch (IOException e) {
            return null;
        }
    }


    public ResponseEntity<Resource> downloadImageFromFileSystem(String fileName) throws IOException {
        File file = new File(FOLDER_PATH, fileName);
        Resource resource = new UrlResource(file.toURI());

        if (resource.exists()) {
            String fileExtension = getFileExtension(fileName);
            MediaType mediaType = determineMediaType(fileExtension);

            if (mediaType != null) {
                return ResponseEntity.ok().contentType(mediaType).body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

    private MediaType determineMediaType(String fileExtension) {
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

//    public byte[] downloadImageFromFileSystem(String fileName) {
//        Optional<Image> imageData = imageRepository.findByUrl(fileName);
//        Path imagePath = Paths.get(FOLDER_PATH + "_" + fileName);
//        System.out.println(imagePath);
//        byte[] images;
//        try {
//            images = Files.readAllBytes(imagePath);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return images;
//    }
//public byte[] downloadImageFromFileSystem(String fileName) {
//    Optional<Image> imageData = imageRepository.findByUrl(fileName);
//    if (imageData.isEmpty()) {
//        throw new RuntimeException("Картинката не е намерена в БД: " + fileName);
//    }
//    Image image = imageData.get();
//    String filePath = FOLDER_PATH + "_" + image.getUrl();
//    Path imagePath = Paths.get(filePath);
//    try {
//        return Files.readAllBytes(imagePath);
//    } catch (IOException e) {
//        throw new RuntimeException("Грешка с разчитането на файла: " + fileName, e);
//    }
//}
//public ResponseEntity<UrlResource> downloadImageFromFileSystem(String fileName) {
//    Path imagePath = Paths.get(FOLDER_PATH + "_" + fileName);
//
//    try {
//        UrlResource resource = new UrlResource(imagePath.toUri());
//
//        if (resource.exists() && resource.isReadable()) {
//            String contentType = Files.probeContentType(imagePath);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.parseMediaType(contentType));
//            headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(resource.contentLength())
//                    .body(resource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    } catch (IOException e) {
//        throw new RuntimeException("Error reading image file: " + fileName, e);
//    }
//}

}
