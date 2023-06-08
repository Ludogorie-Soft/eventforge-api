package com.eventforge.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static com.eventforge.constants.regex.Regex.EVENT_CATEGORIES_PATTERN;
import static com.eventforge.constants.regex.Regex.IMAGE_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {
    @NotNull(message = "Моля прикачете снимка към събитието")
    @Size(max = 5 * 1024 * 1024, message = "Снимката не може да надвишата повече от 5MB.")
    @Pattern(regexp = IMAGE_PATTERN, message = "Файлът трябва да поддържа само(JPG, JPEG, PNG, or GIF).")
    private MultipartFile image;
    @Size(min = 5, max = 30, message = "Името на събитието трява да е между 5 и 30 символа!")
    private String name;
    @Size(min = 5, max = 255, message = "Описанието трява да е между 5 и 255 символа!")
    private String description;
    @Size(min = 5, max = 128, message = "Адресът трява да е между 5 и 128 символа!")
    private String address;
    @Nullable
    @Pattern(regexp = EVENT_CATEGORIES_PATTERN , message = "Моля използвайте само букви (латиница , кирилица) и запетаи")
    private String eventCategories;
    @NotNull
    private Boolean isOnline;
    @FutureOrPresent(message = "Невалидна дата. Не е възможно създаване на събитие с минала дата спрямо текущата!")
    private LocalDateTime startsAt;
    @FutureOrPresent(message = "Невалидна дата. Не е възможно създаване на събитие с минала дата спрямо текущата или от започването му!")
    private LocalDateTime endsAt;
}