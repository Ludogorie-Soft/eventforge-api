//package com.eventforge.service.Impl;
//
//
//import com.eventforge.constants.ImageType;
//import com.eventforge.model.Event;
//import com.eventforge.model.Image;
//import com.eventforge.model.Organisation;
//import com.eventforge.model.User;
//import com.eventforge.repository.ImageRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ImageServiceImplTest {
//    @Mock
//    private ImageRepository imageRepository;
//
//    @InjectMocks
//    private ImageServiceImpl imageService;
//
//    @BeforeEach
//    public void setup() {
//        imageService = new ImageServiceImpl(imageRepository);
//    }
//
//    @Test
//    void saveImageToDb_WithLogo_ShouldSaveLogoImageAndDeleteExistingLogoImage() {
//        // Arrange
//        String logo = "logo_image_url";
//        Organisation org = new Organisation(); // create a test Organisation object
//        User user = new User(); // create a test User object
//        org.setUser(user);
//        Image existingLogoImage = new Image();
//        existingLogoImage.setType(ImageType.LOGO);
//        existingLogoImage.setUrl(logo);
//        existingLogoImage.setOrganisation(org);
//
//        when(imageRepository.findOrganisationLogoByOrgId(org.getId())).thenReturn(existingLogoImage);
//
//        // Act
//        imageService.saveImageToDb(logo, null, null, org, null);
//
//        // Assert
//        verify(imageRepository).delete(existingLogoImage);
//        verify(imageRepository).save(any(Image.class));
//        assertThat(existingLogoImage.getUrl()).isEqualTo(logo);
//        assertThat(existingLogoImage.getType()).isEqualTo(ImageType.LOGO);
//    }
//
//    @Test
//    void saveImageToDb_WithCover_ShouldSaveCoverImageAndDeleteExistingCoverImage() {
//        // Arrange
//        String cover = "cover_image_url";
//        Organisation org = new Organisation(); // create a test Organisation object
//        Image existingCoverImage = new Image();
//        existingCoverImage.setType(ImageType.COVER);
//        existingCoverImage.setUrl(cover);
//        existingCoverImage.setOrganisation(org);
//
//        when(imageRepository.findOrganisationCoverPictureByOrgId(org.getId())).thenReturn(existingCoverImage);
//
//        // Act
//        imageService.saveImageToDb(null, cover, null, org, null);
//
//        // Assert
//        verify(imageRepository).delete(existingCoverImage);
//        verify(imageRepository).save(any(Image.class));
//        assertThat(existingCoverImage.getUrl()).isEqualTo(cover);
//        assertThat(existingCoverImage.getType()).isEqualTo(ImageType.COVER);
//    }
//
//    @Test
//    void saveImageToDb_WithEventPicture_ShouldSaveEventPictureAndDeleteExistingEventPicture() {
//        // Arrange
//        String eventPicture = "event_picture_url";
//        Organisation org = new Organisation(); // create a test Organisation object
//        Event event = new Event(); // create a test Event object
//        event.setOrganisation(org);
//        Image existingEventPicture = new Image();
//        existingEventPicture.setUrl(eventPicture);
//        existingEventPicture.setType(ImageType.EVENT_PICTURE);
//        existingEventPicture.setEvent(event);
//
//        when(imageRepository.findEventPicture(event.getId())).thenReturn(existingEventPicture);
//
//        // Act
//        imageService.saveImageToDb(null, null, eventPicture, org, event);
//
//        // Assert
//        verify(imageRepository).delete(existingEventPicture);
//        verify(imageRepository).save(any(Image.class));
//        assertThat(existingEventPicture.getUrl()).isEqualTo(eventPicture);
//        assertThat(existingEventPicture.getType()).isEqualTo(ImageType.EVENT_PICTURE);
//    }
//
//    @Test
//    void saveImageToDb_WithoutLogoCoverOrEventPicture_ShouldNotSaveAnyImage() {
//        // Arrange
//        Organisation org = new Organisation(); // create a test Organisation object
//
//        // Act
//        imageService.saveImageToDb(null, null, null, org, null);
//
//        // Assert
//        verify(imageRepository, never()).delete(any(Image.class));
//        verify(imageRepository, never()).save(any(Image.class));
//    }
//
//}