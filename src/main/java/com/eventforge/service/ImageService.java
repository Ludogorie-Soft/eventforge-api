package com.eventforge.service;

import com.eventforge.constants.ImageType;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadImageToFileSystem(MultipartFile file, ImageType imageType, Organisation organisation , Event event) throws IOException;



}
