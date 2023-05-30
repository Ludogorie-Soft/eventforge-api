package com.eventforge.service;

import com.eventforge.model.User;
import com.eventforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String email , String password){
        User user = new User(email ,passwordEncoder.encode(password) , true , true);
        return userRepository.save(user);
    }


    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByUsername(email);
    }
}
