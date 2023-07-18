package com.eventforge.dto.request;

import com.eventforge.annotation.UpdatePasswordsMustMatch;
import com.eventforge.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import static com.eventforge.constants.regex.Regex.CREDENTIAL_CHECK_REGEX;




@Getter
@NoArgsConstructor
@UpdatePasswordsMustMatch
public class ChangePasswordRequest {
    @JsonIgnore
    private String currentPassword = UserService.currentUserHashedPassword;
    private String oldPassword;
    @Pattern(regexp = CREDENTIAL_CHECK_REGEX,
            message = "Паролата трябва да е дълга поне 8 знака и да съдържа поне една цифра, една малка буква, една главна буква, един специален знак и без интервали!")
    private String newPassword;

    private String confirmNewPassword;


    public ChangePasswordRequest(String oldPassword, String newPassword, String confirmNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

}
