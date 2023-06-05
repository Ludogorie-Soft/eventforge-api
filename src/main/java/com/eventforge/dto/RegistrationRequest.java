package com.eventforge.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @Email(message = "Моля въведете валидна електронна поща")
    private String username;
    @Size(min=5, max = 30, message = "Името на организацията трябва да е между 5 и 30 символа!")
    private String name;
    @Nullable
    private MultipartFile logo;
    private String bullstat;
    @Nullable
    private Set<String> organisationPriorities;
    @Nullable
    private String optionalCategory;
    private String organisationPurpose;
    @Nullable
    private MultipartFile backgroundCover;
    @Nullable
    private String address;
    @Nullable
    private String website;
    @Nullable
    private String facebookLink;

    private String fullName;

    private String phoneNumber;
    private String charityOption;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,72}$",
            message = "Паролата трябва да е дълга поне 8 знака и да съдържа поне една цифра, една малка буква, една главна буква, един специален знак и без интервали!")
    private String password;
    private String confirmPassword;
}
