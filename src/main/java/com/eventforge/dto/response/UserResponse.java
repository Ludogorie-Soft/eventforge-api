package com.eventforge.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String role;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private boolean isEnabled;
    private boolean isNonLocked;
}
