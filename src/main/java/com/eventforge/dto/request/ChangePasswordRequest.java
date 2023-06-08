package com.eventforge.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.eventforge.constants.regex.Regex.CREDENTIAL_CHECK_REGEX;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {

    private String oldPassword;
    @Pattern(regexp = CREDENTIAL_CHECK_REGEX,
            message = "Паролата трябва да е дълга поне 8 знака и да съдържа поне една цифра, една малка буква, една главна буква, един специален знак и без интервали!")
    private String newPassword;

    private String confirmNewPassword;
}
