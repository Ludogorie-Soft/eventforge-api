package com.eventforge.dto.request;

import com.eventforge.annotation.AgeBoundary;
import com.eventforge.annotation.DateTimeOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.eventforge.constants.regex.Regex.EVENT_CATEGORIES_PATTERN;
import static com.eventforge.constants.regex.Regex.IMAGE_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AgeBoundary
@DateTimeOrder
public class EventRequest {

    @NotNull(message = "Моля прикачете снимка към събитието")
    @Size(max = 5 * 1024 * 1024, message = "Снимката не може да надвишата повече от 5MB.")
    @Pattern(regexp = IMAGE_PATTERN, message = "Файлът трябва да поддържа (JPG, JPEG, PNG, or GIF) формати.")
    private MultipartFile image;
    @Size(min = 5, max = 30, message = "Името на събитието трява да е между 5 и 30 символа!")
    private String name;
    @Size(min = 5, max = 255, message = "Описанието трява да е между 5 и 255 символа!")
    private String description;
    @NotNull(message = "Моля маркирайте полето.")
    private Boolean isOnline;
    @Size(min = 5, max = 255, message = "Полето трява да съдържа поне 5 символа!")
    private String address;
    @Nullable
    @Pattern(regexp = EVENT_CATEGORIES_PATTERN , message = "Моля използвайте само букви (латиница , кирилица) и запетаи")
    private String eventCategories;

    @Nullable
    @PositiveOrZero(message = "Не може да въвеждате отрицателна цена")
    private Double price;
    @Nullable
    @PositiveOrZero(message = "Не можете да въвеждате отрицателни цифри")
    private Integer minAge;
    @Nullable
    @PositiveOrZero(message = "Не можете да въвеждате отрицателни цифри")
    private Integer maxAge;

    @NotNull(message = "Моля маркирайте полето.")
    private Boolean isOneTime;
    @FutureOrPresent(message = "Невалидна дата. Не е възможно създаване на събитие с минала дата спрямо текущата!")
    private LocalDateTime startsAt;
    @FutureOrPresent(message = "Невалидна дата. Не е възможно създаване на събитие с минала дата спрямо текущата!")
    private LocalDateTime endsAt;
    @Nullable
    private String recurrenceDetails;
}