package com.eventforge.service;

import com.eventforge.dto.request.ChangePasswordRequest;
import com.eventforge.dto.request.ResetForgottenPasswordRequest;
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


    public String updateUserIsEnabledFieldAfterConfirmedEmail(String token , String appUrl) {
        String isValid =emailVerificationTokenService.validateVerificationToken(token , appUrl);
        if(isValid == null){
            VerificationToken verificationTokenDb = emailVerificationTokenService.getVerificationTokenByToken(token);
            User user = verificationTokenDb.getUser();
            user.setIsEnabled(true);
            saveUserInDb(user);
            emailVerificationTokenService.deleteVerificationToken(verificationTokenDb);
            log.info("Успешно потвърдена електронна поща - " + user.getUsername());
            return "Успешно потвърдихте профилът си , вече можете да се впишете.";
        }
        return isValid;
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

    public String generateNewRandomPasswordForUserViaVerificationToken(String appUrl , String verificationToken){
        String result = emailVerificationTokenService.validateVerificationToken(verificationToken , appUrl);
        if(result == null){
            VerificationToken token = emailVerificationTokenService.getVerificationTokenByToken(verificationToken);
            User user = token.getUser();
            String newGeneratedPassword = UUID.randomUUID().toString();
            user.setPassword(utils.encodePassword(newGeneratedPassword));
            saveUserInDb(user);
            return newGeneratedPassword;
        }
        return result;
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
