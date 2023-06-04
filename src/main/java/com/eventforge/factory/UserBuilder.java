package com.eventforge.factory;

import com.eventforge.dto.RegistrationRequest;
import com.eventforge.enums.Role;
import com.eventforge.exception.GlobalException;
import com.eventforge.model.User;
import com.eventforge.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserBuilder {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public User createUser(RegistrationRequest request) {
        User user = userService.getUserByEmail(request.getUsername());
        if (user == null) {
            User user1 = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ORGANISATION.toString())
                    .phoneNumber(request.getPhoneNumber())
                    .fullName(request.getFullName())
                    .isEnabled(false)
                    .isNonLocked(true)
                    .build();
            userService.saveUserInDb(user1);
            return user1;
        } else {
            log.warn("Неуспешна регистрация");
            throw new GlobalException(String.format("Потребител с електронна поща %s вече съществува", request.getUsername()));
        }

    }
}
