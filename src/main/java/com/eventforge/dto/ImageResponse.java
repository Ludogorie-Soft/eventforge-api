package com.eventforge.dto;

import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class ImageResponse {
    private UUID id;
    private String url;
    private LocalDateTime uploadAt;
    private LocalDateTime updateAt;
    private String type;
    private Event evenId;
    private Organisation organisationId;
}
