package com.eventforge.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class UserResponse {
    private UUID id;
    private String username;
    private String role;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private boolean isEnabled;
    private boolean isLocked;
}
