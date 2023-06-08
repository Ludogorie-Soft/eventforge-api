package com.eventforge.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

import static com.eventforge.constants.regex.Regex.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAccountRequest {
    @Size(min = 5, max = 30, message = "Името на организацията трябва да е между 5 и 30 символа!")
    private String name;
    @Nullable
    private MultipartFile logo;
    private String bullstat;
    @Nullable
    private Set<String> organisationPriorities;
    @Nullable
    private String optionalCategory;
    @Size (min = 15 , message = "Обоснованието трябва да е поне 15 символа!")
    private String organisationPurpose;
    @Nullable
    private MultipartFile backgroundCover;
    @Nullable
    private String address;
    @Nullable
    @URL(message = "Моля въведете валиден линк")
    private String website;
    @Nullable
    @Pattern(regexp = FACEBOOK_PATTERN , message = "Моля въведете валиден фейсбук линк (www.facebook.com/...)")
    private String facebookLink;
    @Pattern(regexp = FULL_NAME_PATTERN  , message = "Моля използвайте само тирета,букви на кирилица или латиница")
    @Size(min = 8 , message = "Името трябва трябва да съдържа поне 8 символа!")
    private String fullName;
    @Pattern(regexp = PHONE_NUMBER_PATTERN , message = "Невалиден телефонен номер. Позволени са само цифри и знакът +")
    @Size(min = 10 , max = 13 , message = "Невалиден телефонен номер.Телефонният номер трябва да съдържа между 10 и 13 цифри")
    private String phoneNumber;
    @Nullable
    private String charityOption;
}
