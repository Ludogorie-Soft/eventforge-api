package com.eventforge.service.Impl;


import com.eventforge.exception.ImageException;
import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.ImageRepository;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private EventServiceImpl eventService;
    @Mock
    private OrganisationService organisationService;
    @Mock
    private UserService userService;

    @InjectMocks
    private ImageServiceImpl imageService;

    @BeforeEach
    public void setup() {
        imageService = new ImageServiceImpl(imageRepository, userService, organisationService, eventService);
    }

    @Test
    void uploadImageToFileSystem_ShouldReturnException_FileExists() {
        String fileName = "test.jpg";
        MultipartFile file = new MockMultipartFile("file", fileName, "image/jpeg", "test".getBytes());

        when(imageRepository.findImageByUrl(fileName)).thenReturn(Optional.of(new Image()));

        assertThatThrownBy(() -> imageService.uploadImageToFileSystem(file, null, null, null)).isInstanceOf(ImageException.class).hasMessage("Файл с това име вече съществува.");
    }


    @Test
    void uploadImageToFileSystem_ShouldThrowIOException() throws IOException {
        String fileName = "test.jpg";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(fileName);

        when(imageRepository.findImageByUrl(fileName)).thenReturn(Optional.empty());
        when(file.getInputStream()).thenThrow(IOException.class);

        assertThatThrownBy(() -> imageService.uploadImageToFileSystem(file, null, null, null)).isInstanceOf(ImageException.class).hasMessageContaining("Грешка със запазването на файла.");
    }

    @ParameterizedTest
    @CsvSource({"image.jpg, jpg", "image.png, png", "image.jpeg, jpeg"})
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
    void testUpdateEventPicture_EventPresentAndImageExists() {
        Long eventId = 1L;
        Long imageId = 2L;
        String url = "http://example.com/src/image.jpg";
        Organisation organisation = Organisation.builder().user(new User()).build();
        Event event = Event.builder().organisation(organisation).build();
        Image eventPicture = Image.builder().url(url).build();
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);


        when(eventService.findEventById(eventId)).thenReturn(Optional.of(event));
        when(imageRepository.findEventPictureByEventIdImage(eventId, imageId)).thenReturn(eventPicture);

        imageService.updateEventPicture(eventId, imageId, file);

        verify(eventService).findEventById(eventId);
        verify(imageRepository).findEventPictureByEventIdImage(eventId, imageId);
        verify(imageRepository).deleteById(eventPicture.getId());
    }

    @Test
    void testUpdateOrganisationCoverPicture_UserPresentAndCoverExists() {
        String token = "testToken";
        String url = "http://example.com/src/cover.jpg";
        User user = User.builder().id(1L).username("test@example.com").build();
        Organisation organisation = Organisation.builder().id(1L).user(user).build();
        Image cover = Image.builder().url(url).build();
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(organisationService.getOrganisationByUserId(user.getId())).thenReturn(organisation);
        when(imageRepository.findOrganisationCoverPictureByOrgId(organisation.getId())).thenReturn(cover);

        imageService.updateOrganisationCoverPicture(token, file);

        verify(userService).getLoggedUserByToken(token);
        verify(organisationService).getOrganisationByUserId(user.getId());
        verify(imageRepository).deleteById(cover.getId());
    }

    @Test
    void testUpdateOrganisationLogo_UserPresentAndLogoExists() {
        String token = "testToken";
        String url = "http://example.com/src/logo.jpg";
        User user = User.builder().id(1L).username("test@example.com").build();
        Organisation organisation = Organisation.builder().id(1L).user(user).build();
        Image logo = Image.builder().url(url).build();
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(organisationService.getOrganisationByUserId(user.getId())).thenReturn(organisation);
        when(imageRepository.findOrganisationLogoByOrgId(organisation.getId())).thenReturn(logo);

        imageService.updateOrganisationLogo(token, file);

        verify(userService).getLoggedUserByToken(token);
        verify(organisationService).getOrganisationByUserId(user.getId());
        verify(imageRepository).deleteById(logo.getId());
    }

}