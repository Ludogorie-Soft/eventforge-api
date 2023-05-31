package com.eventforge.construct;

import com.eventforge.model.User;
import com.eventforge.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostConstructAdmin {

    private final UserRepository userRepository;


    @PostConstruct
    public void createAdmin(){
        Optional<User> admin = userRepository.findAdmin();
        if(admin.isEmpty()){
            User adminDb = new User("proba@abv.bg" , "admin");
            userRepository.save(adminDb);
        }
    }
}
