package com.eventforge.construct;

import com.eventforge.constants.Constant;
import com.eventforge.constants.Role;
import com.eventforge.model.OrganisationPriority;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationPriorityRepository;
import com.eventforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostConstruct {
    @Value("${spring.admin.username}")
    private  String adminUsername;
    @Value("${spring.admin.password}")
    private  String adminPassword;

    private final OrganisationPriorityRepository organisationPriorityRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @jakarta.annotation.PostConstruct
    public void createAdmin() {
        Optional<User> admin = userRepository.findAdmin();
        if (admin.isEmpty()) {
            User adminDb = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .fullName("admin")
                    .role(Role.ADMIN.toString())
                    .isEnabled(true)
                    .isNonLocked(true)
                    .build();
            userRepository.save(adminDb);
        }
    }


    @jakarta.annotation.PostConstruct
    public void addCategoriesInDataBase() {
        OrganisationPriority organisationPriority;
        for (String category : Constant.staticCategories) {
            if (organisationPriorityRepository.findByCategory(category) == null) {
                organisationPriority = new OrganisationPriority(category);
                organisationPriorityRepository.save(organisationPriority);
            }
        }
    }
}
