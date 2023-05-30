package com.eventforge.email.listener;

import com.eventforge.email.RegistrationCompleteEvent;
import com.eventforge.model.User;
import com.eventforge.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //1. Get the newly registered user
        User theUser = event.getUser();
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser , verificationToken);

        //4. Build the verification url to be sent to the user.
        String url = event.getApplicationUrl()+"/verifyEmail?token="+verificationToken;
        log.info("Линк за потвърждение на регистрация : {} ",url);
        //5. Send  the email.
    }
}
