package com.eventforge.dto.request;

import com.eventforge.annotation.RegistrationOrganisationPriorityNotNull;
import com.eventforge.annotation.PasswordsMustMatch;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

import static com.eventforge.constants.regex.Regex.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RegistrationOrganisationPriorityNotNull
@PasswordsMustMatch
public class RegistrationRequest  {
    @Pattern(regexp = EMAIL_PATTERN, message = "Грешно въведена електронна поща. Трябва да е във формат \"<потребител>@<домейн>.<tld>\"")
    private String username;
    @Size(min = 5, max = 30, message = "Името на организацията трябва да е между 5 и 30 символа!")
    private String name;

    private String bullstat;
    @Nullable
    private Set<String> organisationPriorities;

    @Nullable
    private String optionalCategory;


    @Size(min = 15, message = "Обоснованието трябва да е поне 15 символа!")
    private String organisationPurpose;
    private String logo;
    private String backgroundCover;
//    @Max(value = 5 * 1024 * 1024, message = "Снимката не може да надвишата повече от 5MB.")
//    @Pattern(regexp = IMAGE_PATTERN, message = "Файлът трябва да поддържа (JPG, JPEG, PNG, or GIF) формати.")

    @Nullable
    private String address;
    @Nullable
    @URL(message = "Моля въведете валиден линк")
    private String website;
    @Nullable
    @Pattern(regexp = FACEBOOK_PATTERN, message = "Моля въведете валиден фейсбук линк (www.facebook.com/...)")
    private String facebookLink;
    @Pattern(regexp = FULL_NAME_PATTERN, message = "Моля използвайте само тирета,букви на кирилица или латиница")
    @Size(min = 8, message = "Името трябва трябва да съдържа поне 8 символа!")
    private String fullName;
    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "Невалиден телефонен номер. Позволени са само цифри и знакът +")
    @Size(min = 10, max = 13, message = "Невалиден телефонен номер.Телефонният номер трябва да съдържа между 10 и 13 цифри")
    private String phoneNumber;
    @Nullable
    private String charityOption;
    @Pattern(regexp = CREDENTIAL_CHECK_REGEX,
            message = "Паролата трябва да е дълга поне 8 знака и да съдържа поне една цифра, една малка буква, една главна буква, един специален знак и без интервали!")
    private String password;
    @NotNull
    private String confirmPassword;

}