package com.eventforge.dto.request;

import com.eventforge.annotation.AgeBoundary;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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
    @Size(min = 20, max = 500, message = "Описанието трява да е между 20 и 500 символа!")
    private String description;
    @NotNull(message = "Моля маркирайте полето.")
    private Boolean isOnline;
    @Size(min = 5, max = 255, message = "Полето трява да съдържа поне 5 символа!")
    private String address;
    @Length(min = 3 , message = "Моля въветете поне една ключова дума с поне 3 букви")
    @Pattern(regexp = EVENT_CATEGORIES_PATTERN , message = "Моля използвайте само букви (латиница , кирилица) и запетаи.Не са позволени други символи.")
    private String eventCategories;

    @NotNull (message = "Моля попълнете полето. Ако събитието е безпалтно , въведете 0")
    @PositiveOrZero(message = "Не може да въвеждате отрицателна цена.")
    private Double price;
    @NotNull(message = "Моля попълнете полето. Ако няма възрастово ограничение , моля въведете 0")
    @PositiveOrZero(message = "Не можете да въвеждате отрицателни цифри")
    private Integer minAge;
    @NotNull(message = "Моля попълнете полето. Ако няма възрастово ограничение , моля въведете 0")
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