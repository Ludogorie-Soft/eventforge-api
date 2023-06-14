package com.eventforge.service.Impl;


import com.eventforge.exception.ImageException;
import com.eventforge.model.Image;
import com.eventforge.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
    @Mock
    private ImageRepository imageRepository;
    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    void uploadImageToFileSystem_ShouldReturnException_FileExists() {
        String fileName = "test.jpg";
        MultipartFile file = new MockMultipartFile("file", fileName, "image/jpeg", "test".getBytes());

        when(imageRepository.findImageByName(fileName)).thenReturn(Optional.of(new Image()));

        assertThatThrownBy(() -> imageService.uploadImageToFileSystem(file, null, null, null))
                .isInstanceOf(ImageException.class)
                .hasMessage("Файл с това име вече съществува.");
    }

    @Test
    void uploadImageToFileSystem_ShouldThrowIOException() throws IOException {
        String fileName = "test.jpg";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(fileName);

        when(imageRepository.findImageByName(fileName)).thenReturn(Optional.empty());
        when(file.getInputStream()).thenThrow(IOException.class);

        assertThatThrownBy(() -> imageService.uploadImageToFileSystem(file, null, null, null))
                .isInstanceOf(ImageException.class)
                .hasMessage("Грешка със запазването на файла.");
    }

    @ParameterizedTest
    @CsvSource({
            "image.jpg, jpg",
            "image.png, png",
            "image.jpeg, jpeg"
    })
    void getFileExtension_WithValidFileName_ReturnsExtension(String fileName, String expectedExtension) {
        String extension = imageService.getFileExtension(fileName);
        assertThat(extension).isEqualTo(expectedExtension);
    }

    @Test
    void getFileExtension_WithFileNameWithoutExtension_ReturnsNull() {
        String fileName = "image";
        String extension = imageService.getFileExtension(fileName);
        assertThat(extension).isNull();
    }

    @Test
    void getFileExtension_WithEmptyFileName_ReturnsNull() {
        String fileName = "";
        String extension = imageService.getFileExtension(fileName);
        assertThat(extension).isNull();
    }

    @Test
    void testDetermineMediaType_PNGExtension() {
        String fileExtension = "png";
        MediaType mediaType = imageService.determineMediaType(fileExtension);
        assertThat(mediaType).isEqualTo(MediaType.IMAGE_PNG);
    }

    @Test
    void testDetermineMediaType_NullExtension() {
        String fileExtension = null;
        MediaType mediaType = imageService.determineMediaType(fileExtension);
        assertThat(mediaType).isNull();
    }

    @Test
    void testDetermineMediaType_InvalidExtension() {
        String fileExtension = "bmp";
        assertThatExceptionOfType(ImageException.class).isThrownBy(() -> {
            imageService.determineMediaType(fileExtension);
        }).withMessage("Грешно разширение на файла: bmp");
    }

    @Test
    void testGetImageByName_ImageDoesNotExist() {
        String fileName = "non-existent-image.jpg";
        when(imageRepository.findImageByName(fileName)).thenReturn(Optional.empty());

        Optional<Image> result = imageService.getImageByName(fileName);

        assertThat(result).isEmpty();
    }
    @Test
    void testDetermineMediaType_JPEGExtension() {
        // Arrange
        String fileExtension = "jpeg";

        // Act
        MediaType result = imageService.determineMediaType(fileExtension);

        // Assert
        assertEquals(MediaType.IMAGE_JPEG, result);
    }
}

