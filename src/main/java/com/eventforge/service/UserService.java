package com.eventforge.service;

import com.eventforge.exception.GlobalException;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.repository.UserRepository;
import com.eventforge.security.jwt.JWTService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final JWTService jwtService;

    private  String tokenForCurrentUser;

    private final JWTService jwtService;


    public User getCurrentAuthenticatedUser(){
        String username = jwtService.extractUsernameFromToken(tokenForCurrentUser);
        if(username!= null){
            return getUserByEmail(username);
        }
        return null;
    }
    public void saveUserInDb(User user) {
        userRepository.save(user);
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getLoggedUserByToken(String token){
        String username = jwtService.extractUsernameFromToken(token);
        return getUserByEmail(username);
    }


    public void updateUserIsEnabledFieldAfterConfirmedEmail(User user) {
        if (user != null) {
            user.setIsEnabled(true);
            saveUserInDb(user);
            log.info("Успешно потвърдена електронна поща - " + user.getUsername());
        }

    }

    public void saveUserVerificationToken(User theUser, String token) {
        VerificationToken verificationToken = new VerificationToken(token, theUser);
        emailVerificationTokenService.saveVerificationToken(verificationToken);
    }

    public String validateVerificationToken(String verificationToken, String url) {
        VerificationToken verificationTokenDb = emailVerificationTokenService.getVerificationTokenByToken(verificationToken);
        if (verificationTokenDb == null) {
            throw new GlobalException("Линкът за активация е невалиден");
        }

        User user = verificationTokenDb.getUser();
        Calendar calendar = Calendar.getInstance();
        if (verificationTokenDb.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            return "Линкът за активация е изтекъл , моля кликнете на следният линк за да генерирате нов - " +
                    "<a href=\"" + url + "\"> Генерирай нов линк за активация.</a>";
        }
        updateUserIsEnabledFieldAfterConfirmedEmail(user);
        emailVerificationTokenService.deleteVerificationToken(verificationTokenDb);
        return "Успешно потвърдихте акаунта си";
    }

    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = emailVerificationTokenService.getVerificationTokenByToken(oldToken);
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        emailVerificationTokenService.saveVerificationToken(verificationToken);
        return verificationToken;

    }
}
