package com.eventforge.service.Impl;

import com.eventforge.constants.ImageType;
import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageServiceImpl {
    private final ImageRepository imageRepository;


    public void saveImageToDb(String logo , String cover , String eventPicture , Organisation org , Event event ){
        Image image = new Image();
        Image imageToDelete;
        if(logo!=null){
             imageToDelete = imageRepository.findOrganisationLogoByOrgId(org.getId());
             if(imageToDelete!=null){
                 imageRepository.delete(imageToDelete);
             }
            image.setUrl(logo);
            image.setType(ImageType.LOGO);
            image.setOrganisation(org);
            imageRepository.save(image);
            log.info("Successfully uploaded LOGO for user with email:" + org.getUser().getUsername());
            return;
        }
        if(cover!=null){
            imageToDelete = imageRepository.findOrganisationCoverPictureByOrgId(org.getId());
            if(imageToDelete!=null){
                imageRepository.delete(imageToDelete);
            }
            image.setUrl(cover);
            image.setType(ImageType.COVER);
            image.setOrganisation(org);
            imageRepository.save(image);
            log.info("Successfully uploaded BACKGROUND_COVER for user with email:" + org.getUser().getUsername());
            return;
        }
        if(eventPicture!=null){
            imageToDelete = imageRepository.findEventPicture(event.getId());
            if(imageToDelete!=null){
                imageRepository.delete(imageToDelete);
            }
            image.setUrl(eventPicture);
            image.setType(ImageType.EVENT_PICTURE);
            image.setEvent(event);
            imageRepository.save(image);
            log.info("Successfully uploaded EVENT_PICTURE for user with email:" + event.getOrganisation().getUser().getUsername());
            return;
        }
        log.info("Unsuccessful attempt to upload picture!!");
    }
}
