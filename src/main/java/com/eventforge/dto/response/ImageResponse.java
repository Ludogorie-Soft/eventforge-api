package com.eventforge.dto.response;

import com.eventforge.model.Organisation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ImageResponse {
    private Long id;
    private String url;
    private LocalDateTime uploadAt;
    private LocalDateTime updateAt;
    private String type;
    private Long evenId;
    private Organisation organisationId;
}
