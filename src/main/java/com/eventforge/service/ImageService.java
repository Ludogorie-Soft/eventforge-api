package com.eventforge.service;

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
public class ImageService {
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
        Image newImage = new Image();
        Image imageToUpdate;
        if(logo!=null){
             imageToUpdate = imageRepository.findOrganisationLogoByOrgId(org.getId());
             if(imageToUpdate!=null){
                 imageToUpdate.setUrl(logo);
                 imageRepository.save(imageToUpdate);
                 return;
             }
            newImage.setUrl(logo);
            newImage.setType(ImageType.LOGO);
            newImage.setOrganisation(org);
            imageRepository.save(newImage);
            return;
        }
        if(cover!=null){
            imageToUpdate = imageRepository.findOrganisationCoverPictureByOrgId(org.getId());
            if(imageToUpdate!=null){
                imageToUpdate.setUrl(cover);
                imageRepository.save(imageToUpdate);
                return;
            }
            newImage.setUrl(cover);
            newImage.setType(ImageType.COVER);
            newImage.setOrganisation(org);
            imageRepository.save(newImage);
            return;
        }
        if(eventPicture!=null){
            imageToUpdate = imageRepository.findEventPicture(event.getId());
            if(imageToUpdate!=null){
                imageToUpdate.setUrl(eventPicture);
                imageRepository.save(imageToUpdate);
                return;
            }
            newImage.setUrl(eventPicture);
            newImage.setType(ImageType.EVENT_PICTURE);
            newImage.setEvent(event);
            imageRepository.save(newImage);
            return;
        }
        log.info("Unsuccessful attempt to upload picture!!");
    }
}
