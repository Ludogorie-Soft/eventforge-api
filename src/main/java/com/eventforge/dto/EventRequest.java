package com.eventforge.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {
    private UUID id;
    @Size(min = 5, max = 30, message = "Името на събитието трява да е между 5 и 30 символа!")
    private String name;
    @Size(min = 5, max = 255, message = "Описанието трява да е между 5 и 255 символа!")
    private String description;
    @Size(min = 5, max = 128,message = "Адресът трява да е между 5 и 128 символа!")
    private String address;
    private List<String> eventCategories;
    @NotBlank
    @NotNull
    private UUID organisationId;
    @NotBlank
    @NotNull
    private boolean isOnline;
    @FutureOrPresent(message = "Трябва да въведете текуща или дата!")
    private LocalDateTime startsAt;
    @FutureOrPresent(message = "Трябва да въведете текуща или дата!")
    private LocalDateTime endsAt;
}