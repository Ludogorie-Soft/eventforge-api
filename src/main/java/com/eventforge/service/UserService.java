package com.eventforge.service;

import com.eventforge.exception.GlobalException;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.repository.UserRepository;
import com.eventforge.repository.VerificationTokenRepository;
import jakarta.persistence.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;


    public void saveUserInDb(User user){
        userRepository.save(user);
    }


    public Optional<User> getOptionalUserByEmail(String email) {
        return userRepository.findByUsername(email);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void updateUserIsEnabledFieldAfterConfirmedEmail(User user){
        if(user!=null){
            user.setEnabled(true);
            saveUserInDb(user);
            log.info("Успешно потвърдена електронна поща - "+user.getUsername());
        }else {
            log.warn("Не е намерен потребител с електронна поща "+user.getUsername());
        }

    }

    public void saveUserVerificationToken(User theUser, String token) {
        VerificationToken verificationToken = new VerificationToken(token , theUser);
        verificationTokenRepository.save(verificationToken);
    }

    public String validateVarificationToken(String verificationToken) {
        VerificationToken verificationTokenDb = verificationTokenRepository.findByToken(verificationToken);
        if(verificationTokenDb == null){
            throw new GlobalException("Линкът за активация е невалиден");
        }
        User user = verificationTokenDb.getUser();
        Calendar calendar =Calendar.getInstance();
        if(verificationTokenDb.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0){
            verificationTokenRepository.delete(verificationTokenDb);
            throw new GlobalException("Линкът за активация е изтекъл");
        }
        updateUserIsEnabledFieldAfterConfirmedEmail(user);
        return "Успешно потвърдихте акаунта си";
    }
}
