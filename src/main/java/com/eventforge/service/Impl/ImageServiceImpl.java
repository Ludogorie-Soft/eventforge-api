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

    public Image findEventImageByUrlAndEventId(String url , Long eventId){
        return imageRepository.findEventImageByUrlAndEventId(url , eventId);
    }

    public Image findLogoByUrlAndOrgId(String url , Long orgId){
        return imageRepository.findLogoByUrlAndOrgId(url , orgId);
    }

    public Image findCoverByUrlAndOrgId(String url , Long orgId){
        return imageRepository.findCoverByUrlAndOrgId(url , orgId);
    }

    public void saveImageToDb(String logo , String cover , String eventPicture , Organisation org , Event event ){
        Image image = new Image();
        Image imageToUpdate;
        if(logo!=null){
             imageToUpdate = imageRepository.findOrganisationLogoByOrgId(org.getId());
             if(imageToUpdate!=null){
                 imageToUpdate.setUrl(logo);
                 imageRepository.save(imageToUpdate);
                 return;
             }
            image.setUrl(logo);
            image.setType(ImageType.LOGO);
            image.setOrganisation(org);
            imageRepository.save(image);
//            log.info("Successfully uploaded LOGO for user with email:" + org.getUser().getUsername());
            return;
        }
        if(cover!=null){
            imageToUpdate = imageRepository.findOrganisationCoverPictureByOrgId(org.getId());
            if(imageToUpdate!=null){
                imageToUpdate.setUrl(cover);
                imageRepository.save(imageToUpdate);
                return;
            }
            image.setUrl(cover);
            image.setType(ImageType.COVER);
            image.setOrganisation(org);
            imageRepository.save(image);
//            log.info("Successfully uploaded BACKGROUND_COVER for user with email:" + org.getUser().getUsername());
            return;
        }
        if(eventPicture!=null){
            imageToUpdate = imageRepository.findEventPicture(event.getId());
            if(imageToUpdate!=null){
                imageToUpdate.setUrl(eventPicture);
                imageRepository.save(imageToUpdate);
                return;
            }
            image.setUrl(eventPicture);
            image.setType(ImageType.EVENT_PICTURE);
            image.setEvent(event);
            imageRepository.save(image);
//            log.info("Successfully uploaded EVENT_PICTURE for user with email:" + event.getOrganisation().getUser().getUsername());
            return;
        }
        log.info("Unsuccessful attempt to upload picture!!");
    }
}
