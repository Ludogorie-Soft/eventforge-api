package com.eventforge.dto.request;

import com.eventforge.annotation.IsEmailFree;
import com.eventforge.annotation.RegistrationOrganisationPriorityNotNull;
import com.eventforge.annotation.RegistrationPasswordsMustMatch;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

import static com.eventforge.constants.regex.Regex.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RegistrationOrganisationPriorityNotNull
@RegistrationPasswordsMustMatch
public class RegistrationRequest  {
    @Pattern(regexp = EMAIL_PATTERN, message = "Грешно въведена електронна поща. Трябва да е във формат \"<потребител>@<домейн>.<tld>\"")
    @IsEmailFree
    private String username;
    @Size(min = 5, max = 30, message = "Името на организацията трябва да е между 5 и 30 символа!")
    private String name;

    private String bullstat;
    @Nullable
    private Set<String> organisationPriorities;

    @Nullable
    @Pattern(regexp = EVENT_CATEGORIES_PATTERN , message = "Моля използвайте само букви (латиница , кирилица) и запетаи.Не са позволени други символи.")
    private String optionalCategory;


    @Size(min = 50, message = "Съдържанието на полето трябва да е поне 50 символа!")
    private String organisationPurpose;
    private String logo;
    private String backgroundCover;

    @Nullable
    private String address;
    @Nullable
    @URL (message = "Разрешени са само URL адреси/линкове.")
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
    @URL (message = "Разрешени са само URL адреси/линкове.")
    private String charityOption;
    @Pattern(regexp = CREDENTIAL_CHECK_REGEX,
            message = "Паролата трябва да е дълга поне 8 знака и да съдържа поне една цифра, една малка буква, една главна буква, един специален знак и без интервали!")
    private String password;
    @NotNull
    private String confirmPassword;

}