package com.eventforge.construct;

import com.eventforge.constants.OrganisationPriorityCategory;
import com.eventforge.constants.Role;
import com.eventforge.model.OrganisationPriority;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationPriorityRepository;
import com.eventforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostConstruct {
    @Value("${spring.admin.username}")
    private  String ADMIN_USERNAME;
    @Value("${spring.admin.password}")
    private  String ADMIN_PASSWORD;

    private final OrganisationPriorityRepository organisationPriorityRepository;

    private final UserRepository userRepository;
    private HashSet<String> categories = OrganisationPriorityCategory.categories;

    private final PasswordEncoder passwordEncoder;

    @jakarta.annotation.PostConstruct
    public void createAdmin() {
        Optional<User> admin = userRepository.findAdmin();
        if (admin.isEmpty()) {
            User adminDb = User.builder()
                    .username(ADMIN_USERNAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
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
        for (String category : categories) {
            if (organisationPriorityRepository.findByCategory(category) == null) {
                organisationPriority = new OrganisationPriority(category);
                organisationPriorityRepository.save(organisationPriority);
            }
        }
    }
}
