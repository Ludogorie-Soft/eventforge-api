package com.eventforge.dto.request;

import com.eventforge.model.Organisation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageRequest {
    private String url;
    private String type;
    @NotNull
    @NotBlank
    private Long evenId;
    @NotNull
    @NotBlank
    private Organisation organisationId;
}
