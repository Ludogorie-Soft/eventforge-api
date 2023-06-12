package com.eventforge.service.Impl;

import com.eventforge.exception.GlobalException;
import com.eventforge.model.Image;
import com.eventforge.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    @BeforeEach
    public void setup() {
        imageService = new ImageServiceImpl(imageRepository);
    }

    @Test
    void uploadImageToFileSystem_ShouldReturnException_FileExists() {
        String fileName = "test.jpg";
        MultipartFile file = new MockMultipartFile("file", fileName, "image/jpeg", "test".getBytes());

        when(imageRepository.findImageByName(fileName)).thenReturn(Optional.of(new Image()));

        assertThatThrownBy(() -> imageService.uploadImageToFileSystem(file))
                .isInstanceOf(GlobalException.class)
                .hasMessage("Файл с това име вече съществува.");
    }


    @Test
    void uploadImageToFileSystem_ShouldThrowIOException() throws IOException {
        String fileName = "test.jpg";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(fileName);

        when(imageRepository.findImageByName(fileName)).thenReturn(Optional.empty());
        when(file.getInputStream()).thenThrow(IOException.class);

        assertThatThrownBy(() -> imageService.uploadImageToFileSystem(file))
                .isInstanceOf(GlobalException.class)
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
        String expectedExtension = null;

        String extension = imageService.getFileExtension(fileName);

        assertThat(extension).isEqualTo(expectedExtension);
    }

    @Test
    void getFileExtension_WithEmptyFileName_ReturnsNull() {
        String fileName = "";
        String expectedExtension = null;

        String extension = imageService.getFileExtension(fileName);

        assertThat(extension).isEqualTo(expectedExtension);
    }

}