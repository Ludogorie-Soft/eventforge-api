package com.eventforge.service.Impl;

import com.eventforge.constants.ImageType;
import com.eventforge.exception.ImageException;
import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.ImageRepository;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageServiceImpl {
    private static final String FOLDER_PATH = "src/main/resources/static/images/";


    private final ImageRepository imageRepository;

    private final UserService userService;

    private final OrganisationService organisationService;

    private final EventServiceImpl eventService;

    public void uploadImageToFileSystem(MultipartFile file, ImageType imageType, Organisation organisation, Event event) {
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
        String imageUrlAbsolutePath = filePath.toAbsolutePath().toString();

        try {
            if (!Files.exists(uploadDirectory)) {
                Files.createDirectories(uploadDirectory);
            }
            if (Files.exists(uploadDirectory)) {
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new ImageException("Грешка със запазването на файла. Моля уверете се , че подавате валиден файл и не е празен.");
        }

        if (organisation != null && (imageType != null && imageType.equals(ImageType.LOGO))) {
            imageRepository.save(Image.builder()
                    .url(imageUrlAbsolutePath)
                    .uploadAt(LocalDateTime.now())
                    .type(imageType)
                    .organisation(organisation)
                    .build());
            log.info("Successfully uploaded picture for user with email:" + organisation.getUser().getUsername());
            return;
        }
        if (organisation != null && (imageType != null && imageType.equals(ImageType.COVER))) {
            imageRepository.save(Image.builder()
                    .url(imageUrlAbsolutePath)
                    .uploadAt(LocalDateTime.now())
                    .type(imageType)
                    .organisation(organisation)
                    .build());
            log.info("Successfully uploaded picture for user with email:" + organisation.getUser().getUsername());
            return;
        }

        if (event != null && (imageType != null && imageType.equals(ImageType.EVENT_PICTURE))) {
            imageRepository.save(Image.builder()
                    .url(imageUrlAbsolutePath)
                    .uploadAt(LocalDateTime.now())
                    .type(imageType)
                    .event(event)
                    .build());
            log.info("Successfully uploaded picture for user with email:" + event.getOrganisation().getUser().getUsername());
            return;

        }
        log.info("Unsuccessful attempt to upload picture!!");
    }

    public void updateOrganisationLogo(String token, MultipartFile file) {
        User user = userService.getLoggedUserByToken(token);
        if (user != null) {
            Organisation organisation = organisationService.getOrganisationByUserId(user.getId());
            Image logo = imageRepository.findOrganisationLogoByOrgId(organisation.getId());
            uploadImageToFileSystem(file, ImageType.LOGO, organisation, null);
            if (logo != null) {
                String filePath = logo.getUrl().substring(logo.getUrl().indexOf("src"));
                deleteImageFile(filePath);
                imageRepository.deleteById(logo.getId());
            }
            log.info("Successfully updated logo picture for user with email:" + user.getUsername());
            return;
        }

        log.info("Unsuccessful attempt to upload picture!!");

    }

    public void updateOrganisationCoverPicture(String token, MultipartFile file) {
        User user = userService.getLoggedUserByToken(token);
        if (user != null) {
            Organisation organisation = organisationService.getOrganisationByUserId(user.getId());
            Image cover = imageRepository.findOrganisationCoverPictureByOrgId(organisation.getId());
            uploadImageToFileSystem(file, ImageType.COVER, organisation, null);
            if (cover != null) {
                String filePath = cover.getUrl().substring(cover.getUrl().indexOf("src"));

                deleteImageFile(filePath);
                imageRepository.deleteById(cover.getId());
            }
            log.info("Successfully updated cover picture for user with email:" + user.getUsername());
            return;
        }
        log.info("Unsuccessful attempt to upload picture!!");

    }

    public void updateEventPicture(Long eventId, Long imageId, MultipartFile file) {
        Optional<Event> event = eventService.findEventById(eventId);

        if (event.isPresent()) {
            Image eventPicture = imageRepository.findEventPictureByEventIdImage(eventId, imageId);
            uploadImageToFileSystem(file, ImageType.EVENT_PICTURE, null, event.get());
            if (eventPicture != null) {
                String filePath = eventPicture.getUrl().substring(eventPicture.getUrl().indexOf("src"));

                deleteImageFile(filePath);
                imageRepository.deleteById(eventPicture.getId());
            }
            log.info("Successfully uploaded event picture for user!");
            return;
        }
        log.info("Unsuccessful attempt to upload picture!!");
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
        Optional<Image> imageOptional = imageRepository.findImageByUrl(fileName);
        return imageOptional.isPresent();
    }

    @Nullable
    String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

    protected void deleteImageFile(String filePath) {
        Path fileToDelete = Paths.get(filePath);
        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            throw new ImageException("Грешка при опит за изтриването на файла.");
        }
    }
    public static String encodeImage(String url) {
        String base64Image = "";
        File file = new File(url);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            byte[] imageData = new byte[(int) file.length()];
            int bytesRead;
            int totalBytesRead = 0;

            while ((bytesRead = imageInFile.read(imageData, totalBytesRead, imageData.length - totalBytesRead)) != -1) {
                totalBytesRead += bytesRead;
                if (totalBytesRead == imageData.length) {
                    break;
                }
            }
            base64Image = Base64.getEncoder().encodeToString(Arrays.copyOf(imageData, totalBytesRead));
            log.info("Total bytes read: " + totalBytesRead);
        } catch (FileNotFoundException e) {
            throw new ImageException("Изображението не е намерено");
        } catch (IOException e) {
            throw new ImageException("Грешка с прочитането на изображението");
        }
        return base64Image;
    }
}
