package com.eventforge.service.Impl;

import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    private ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageServiceImpl(imageRepository);
    }

    @Test
    void findEventImageByUrlAndEventId_ShouldInvokeRepositoryMethodAndReturnImage() {
        // Arrange
        Long eventId = 1L;
        String url = "example.com/image.jpg";
        Image expectedImage = new Image();
        when(imageRepository.findEventImageByUrlAndEventId(eq(url), eq(eventId))).thenReturn(expectedImage);

        // Act
        Image result = imageService.findEventImageByUrlAndEventId(url, eventId);

        // Assert
        assertEquals(expectedImage, result);
        verify(imageRepository, times(1)).findEventImageByUrlAndEventId(eq(url), eq(eventId));
    }

    @Test
    void findLogoByUrlAndOrgId_ShouldInvokeRepositoryMethodAndReturnImage() {
        // Arrange
        Long orgId = 1L;
        String url = "example.com/logo.jpg";
        Image expectedImage = new Image();
        when(imageRepository.findLogoByUrlAndOrgId(eq(url), eq(orgId))).thenReturn(expectedImage);

        // Act
        Image result = imageService.findLogoByUrlAndOrgId(url, orgId);

        // Assert
        assertEquals(expectedImage, result);
        verify(imageRepository, times(1)).findLogoByUrlAndOrgId(eq(url), eq(orgId));
    }

    @Test
    void findCoverByUrlAndOrgId_ShouldInvokeRepositoryMethodAndReturnImage() {
        // Arrange
        Long orgId = 1L;
        String url = "example.com/cover.jpg";
        Image expectedImage = new Image();
        when(imageRepository.findCoverByUrlAndOrgId(eq(url), eq(orgId))).thenReturn(expectedImage);

        // Act
        Image result = imageService.findCoverByUrlAndOrgId(url, orgId);

        // Assert
        assertEquals(expectedImage, result);
        verify(imageRepository, times(1)).findCoverByUrlAndOrgId(eq(url), eq(orgId));
    }

    @Test
    void saveImageToDb_ShouldSaveLogoImageWhenLogoIsNotNullAndImageExists() {
        // Arrange
        String logo = "example.com/logo.jpg";
        Organisation org = new Organisation();
        org.setId(1L);
        Image existingImage = new Image();
        existingImage.setOrganisation(org);
        when(imageRepository.findOrganisationLogoByOrgId(eq(org.getId()))).thenReturn(existingImage);

        // Act
        imageService.saveImageToDb(logo, null, null, org, null);

        // Assert
        verify(imageRepository, times(1)).findOrganisationLogoByOrgId(eq(org.getId()));
        verify(imageRepository, times(1)).save(eq(existingImage));
    }

    @Test
    void saveImageToDb_ShouldSaveLogoImageWhenLogoIsNotNullAndImageDoesNotExist() {
        // Arrange
        String logo = "example.com/logo.jpg";
        Organisation org = new Organisation();
        org.setId(1L);

        // Act
        imageService.saveImageToDb(logo, null, null, org, null);

        // Assert
        verify(imageRepository, times(1)).findOrganisationLogoByOrgId(eq(org.getId()));
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void saveImageToDb_ShouldSaveCoverImageWhenCoverIsNotNullAndImageExists() {
        // Arrange
        String cover = "example.com/cover.jpg";
        Organisation org = new Organisation();
        org.setId(1L);
        Image existingImage = new Image();
        existingImage.setOrganisation(org);
        when(imageRepository.findOrganisationCoverPictureByOrgId(eq(org.getId()))).thenReturn(existingImage);

        // Act
        imageService.saveImageToDb(null, cover, null, org, null);

        // Assert
        verify(imageRepository, times(1)).findOrganisationCoverPictureByOrgId(eq(org.getId()));
        verify(imageRepository, times(1)).save(eq(existingImage));
    }

    @Test
    void saveImageToDb_ShouldSaveCoverImageWhenCoverIsNotNullAndImageDoesNotExist() {
        // Arrange
        String cover = "example.com/cover.jpg";
        Organisation org = new Organisation();
        org.setId(1L);

        // Act
        imageService.saveImageToDb(null, cover, null, org, null);

        // Assert
        verify(imageRepository, times(1)).findOrganisationCoverPictureByOrgId(eq(org.getId()));
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void saveImageToDb_ShouldSaveEventPictureWhenEventPictureIsNotNullAndImageExists() {
        // Arrange
        String eventPicture = "example.com/event.jpg";
        Event event = new Event();
        event.setId(1L);
        Image existingImage = new Image();
        existingImage.setEvent(event);
        when(imageRepository.findEventPicture(eq(event.getId()))).thenReturn(existingImage);

        // Act
        imageService.saveImageToDb(null, null, eventPicture, null, event);

        // Assert
        verify(imageRepository, times(1)).findEventPicture(eq(event.getId()));
        verify(imageRepository, times(1)).save(eq(existingImage));
    }

    @Test
    void saveImageToDb_ShouldSaveEventPictureWhenEventPictureIsNotNullAndImageDoesNotExist() {
        // Arrange
        String eventPicture = "example.com/event.jpg";
        Event event = new Event();
        event.setId(1L);

        // Act
        imageService.saveImageToDb(null, null, eventPicture, null, event);

        // Assert
        verify(imageRepository, times(1)).findEventPicture(eq(event.getId()));
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void saveImageToDb_ShouldNotSaveAnyImageWhenAllParametersAreNull() {
        // Act
        imageService.saveImageToDb(null, null, null, null, null);

        // Assert
        verifyNoInteractions(imageRepository);
    }

    @Test
    void saveImageToDb_ShouldNotSaveAnyImageWhenAllImageParametersAreNull() {
        // Arrange
        Organisation org = new Organisation();

        // Act
        imageService.saveImageToDb(null, null, null, org, null);

        // Assert
        verifyNoInteractions(imageRepository);
    }

    @Test
    void saveImageToDb_ShouldNotSaveAnyImageWhenAllImageParametersAndEventAreNull() {
        // Arrange
        Organisation org = new Organisation();

        // Act
        imageService.saveImageToDb(null, null, null, org, null);

        // Assert
        verifyNoInteractions(imageRepository);
    }

}
