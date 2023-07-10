package com.eventforge.dto.request;

import com.eventforge.annotation.AgeBoundary;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.eventforge.constants.regex.Regex.EVENT_CATEGORIES_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AgeBoundary
public class EventRequest {

    private String imageUrl;
    @Size(min = 5, max = 30, message = "Името на събитието трява да е между 5 и 30 символа!")
    private String name;
    @Size(min = 100, max = 500, message = "Описанието трява да е между 100 и 500 символа!")
    private String description;
    @NotNull(message = "Моля маркирайте полето.")
    private Boolean isOnline;
    @Size(min = 5, max = 255, message = "Полето трява да съдържа поне 5 символа!")
    private String address;
    @Nullable
    @Pattern(regexp = EVENT_CATEGORIES_PATTERN , message = "Моля използвайте само букви (латиница , кирилица) и запетаи.Не са позволени други символи.")
    private String eventCategories;

    @Nullable
    @PositiveOrZero(message = "Не може да въвеждате отрицателна сума")
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
    @FutureOrPresent(message = "Невалидна дата. Не е възможно създаване на събитие с минала дата спрямо текущата или с минала дата спрямо започване на събитието!")
    private LocalDateTime endsAt;
    @Nullable
    private String recurrenceDetails;
}