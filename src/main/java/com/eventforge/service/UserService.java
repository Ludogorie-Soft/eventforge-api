package com.eventforge.service;

import com.eventforge.dto.request.ChangePasswordRequest;
import com.eventforge.exception.InvalidEmailConfirmationLinkException;
import com.eventforge.exception.InvalidPasswordException;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.repository.UserRepository;
import com.eventforge.security.jwt.JWTService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final JWTService jwtService;
    private final Utils utils;


    public void saveUserInDb(User user) {
        userRepository.save(user);
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getLoggedUserByToken(String token) {
        String extractedTokenFromHeader = jwtService.extractTokenValueFromHeader(token);
        String username = jwtService.extractUsernameFromToken(extractedTokenFromHeader);
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

    public String changeAccountPassword(String token, ChangePasswordRequest request) {
        User user = getLoggedUserByToken(token);
        if (user != null) {
            if (!utils.isPasswordValid(request.getOldPassword(), user.getPassword())) {
                log.info("Unsuccessful attempt to change the password for user" + user.getUsername());
                throw new InvalidPasswordException("Паролата не съответства на запазената в базата данни.");
            }
            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                log.info("Unsuccessful attempt to change the password for user" + user.getUsername());
                throw new InvalidPasswordException("Новите пароли не съвпадат. Новата парола трябва да съответства на потвърдената парола.");
            }
            String encodedPassword = utils.encodePassword(request.getNewPassword());
            user.setPassword(encodedPassword);
            saveUserInDb(user);
            log.info("Паролата за потребител " + user.getUsername() + " е променена успешно.");
            return "Успешно променихте паролата си.";
        }
        return null;
    }

    public String validateVerificationToken(String verificationToken, String url) {
        VerificationToken verificationTokenDb = emailVerificationTokenService.getVerificationTokenByToken(verificationToken);
        if (verificationTokenDb == null) {
            throw new InvalidEmailConfirmationLinkException();
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

    public void setApproveByAdminToTrue(Long userId){
        Optional<User> user =userRepository.findById(userId);
        if(user.isPresent()){
            user.get().setIsApprovedByAdmin(true);
            saveUserInDb(user.get());
            log.info("Account with email {} was approved by the site administrator",user.get().getUsername());
        }
    }

    public void lockAccountById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            user.get().setIsNonLocked(false);
            saveUserInDb(user.get());
            log.info("Account with email {} has been locked by the site administrator" , user.get().getUsername());
        }
    }

    public void unlockAccountById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            user.get().setIsNonLocked(true);
            saveUserInDb(user.get());
            log.info("Account with email {} has been unlocked by the site administrator" ,user.get().getUsername());
        }
    }
}
