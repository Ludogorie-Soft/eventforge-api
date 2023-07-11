package com.eventforge.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import static com.eventforge.constants.regex.Regex.EMAIL_PATTERN;
import static com.eventforge.constants.regex.Regex.PHONE_NUMBER_PATTERN;

@Getter
@Setter
@Builder
public class EventEnrollmentRequest {
    @NotNull
    private Long eventId;

    @Pattern(regexp = PHONE_NUMBER_PATTERN , message = "Невалиден телефонен номер. Позволени са само цифри и знакът +")
    @Size(min = 10 , max = 13 , message = "Невалиден телефонен номер.Телефонният номер трябва да съдържа между 10 и 13 цифри")
    @Nullable
    private String phone;

    @URL(message = "Моля въведете валиден линк")
    @Nullable
    private String externalLink;

    @Nullable
    @Pattern(regexp = EMAIL_PATTERN,message = "Грешно въведена електронна поща. Трябва да е във формат \"<потребител>@<домейн>.<tld>\"")
    private String email;
}
