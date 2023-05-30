package com.eventforge.dto;

import com.eventforge.enums.OrganisationPriorityCategory;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;

@Data
@Builder
public class RegistrationRequest {
    @Email(message = "Моля въведете валидна електронна поща")
    private String email;
    @Size(min=5, max = 30, message = "Името на организацията трябва да е между 5 и 30 символа!")
    private String name;
    @Nullable
    private MultipartFile logo;
    private String bullstat;
    @Nullable
    private HashSet<String> categories = OrganisationPriorityCategory.categories;
    @Nullable
    private String optionalCategory;
    private String purposeOfOrganisation;
    @Nullable
    private MultipartFile backgroundCover;
    @Nullable
    private String address;
    @Nullable
    private String website;
    @Nullable
    private String facebookLink;

    private String firstAndLastName;

    private String phoneNumber;
    private String optionalCharity;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Паролата трябва да е дълга поне 8 знака и да съдържа поне една цифра, една малка буква, една главна буква, един специален знак и без интервали!")
    private String password;
    private String confirmPassword;
}
