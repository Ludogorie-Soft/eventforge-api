package com.eventforge.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {
    @Size(min = 5, max = 30, message = "Името на събитието трява да е между 5 и 30 символа!")
    private String name;
    @Size(min = 5, max = 255, message = "Описанието трява да е между 5 и 255 символа!")
    private String description;
    @Size(min = 5, max = 128, message = "Адресът трява да е между 5 и 128 символа!")
    private String address;
    private List<String> eventCategories;
    private String organisationName;
    @NotNull
    private Boolean isOnline;
    @FutureOrPresent(message = "Трябва да въведете текуща или дата!")
    private LocalDateTime startsAt;
    @FutureOrPresent(message = "Трябва да въведете текуща или дата!")
    private LocalDateTime endsAt;
}