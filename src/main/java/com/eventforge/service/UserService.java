package com.eventforge.service;

import com.eventforge.dto.request.ChangePasswordRequest;
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


    public String updateUserIsEnabledFieldAfterConfirmedEmail(String token) {

            VerificationToken verificationTokenDb = emailVerificationTokenService.getVerificationTokenByToken(token);
            emailVerificationTokenService.validateTokenExpirationTime(verificationTokenDb);
            User user = verificationTokenDb.getUser();
            user.setIsEnabled(true);
            saveUserInDb(user);
            emailVerificationTokenService.deleteVerificationToken(verificationTokenDb);
            log.info("Успешно потвърдена електронна поща - " + user.getUsername());
            return "Успешно потвърдихте профилът си , вече можете да се впишете.";

    }

    public void saveUserVerificationToken(User theUser, String token , String type ) {
        VerificationToken verificationToken;
        if(theUser.getVerificationToken()==null){
           verificationToken= new VerificationToken(token, theUser , type);
        } else {
            verificationToken = theUser.getVerificationToken();
            verificationToken.setToken(token);
        }
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

    public String generateNewRandomPasswordForUserViaVerificationToken(VerificationToken token , User user){
            emailVerificationTokenService.validateTokenExpirationTime(token);

            String newGeneratedPassword = utils.generateRandomPassword();
            user.setPassword(utils.encodePassword(newGeneratedPassword));
            saveUserInDb(user);
            emailVerificationTokenService.deleteVerificationToken(token);
        return newGeneratedPassword;
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
