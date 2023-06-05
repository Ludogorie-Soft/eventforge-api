//package com.eventforge.service;
//
//import com.eventforge.model.User;
//import com.eventforge.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//    private UserService userService;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private EmailVerificationTokenService emailVerificationTokenService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        userService = new UserService(userRepository, emailVerificationTokenService);
//    }
//
//    @Test
//    void testSaveUserInDb() {
//        User user = User.builder().name("ivan").build();
//        userService.saveUserInDb(user);
//
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    void testUpdateUserIsEnabledFieldAfterConfirmedEmail() {
//        User user = User.builder().name("ivan").isEnabled(false).build();
//
//        userService.updateUserIsEnabledFieldAfterConfirmedEmail(user);
//
//        assertThat(user.getIsEnabled()).isTrue();
//
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    void testUpdateUserIsEnabledFieldAfterConfirmedEmail_UserNull() {
//        userService.updateUserIsEnabledFieldAfterConfirmedEmail(null);
//
//        verifyNoInteractions(userRepository);
//    }
//
//    @Test
//    void testSaveUserVerificationToken() {
//        User user = User.builder().name("ivan").isEnabled(false).build();
//        String token = "abc123";
//
//        userService.saveUserVerificationToken(user, token);
//
//        verify(emailVerificationTokenService).saveVerificationToken(
//                argThat(verificationToken ->
//                        verificationToken.getToken().equals(token) &&
//                                verificationToken.getUser().equals(user)
//                )
//        );
//    }
//}