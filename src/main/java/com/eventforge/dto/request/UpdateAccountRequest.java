package com.eventforge.dto.request;

import com.eventforge.annotation.UpdateOrganisationPriorityNotNull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

import static com.eventforge.constants.regex.Regex.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@UpdateOrganisationPriorityNotNull
public class UpdateAccountRequest  {
    @Size(min = 5, max = 35, message = "Името на организацията трябва да е между 5 и 35 символа!")
    private String name;

    private String bullstat;
    @Nullable
    private Set<String> chosenPriorities;
    @Nullable
    @Pattern(regexp = EVENT_CATEGORIES_PATTERN , message = "Моля използвайте само букви (латиница , кирилица) и запетаи.Не са позволени други символи.")
    private String optionalCategory;
    @Size(min = 50, message = "Съдържанието на полето трябва да е поне 50 символа!")
    private String organisationPurpose;

    @Nullable
    private String address;
    @Nullable
    @URL (message = "Разрешени са само URL адреси/линкове.")
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
    @URL (message = "Разрешени са само URL адреси/линкове.")
    private String charityOption;
    private Set<String> allPriorities;
}
