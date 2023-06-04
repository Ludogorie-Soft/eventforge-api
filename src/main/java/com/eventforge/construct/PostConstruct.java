package com.eventforge.construct;

import com.eventforge.enums.OrganisationPriorityCategory;
import com.eventforge.enums.Role;
import com.eventforge.model.OrganisationPriority;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationPriorityRepository;
import com.eventforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostConstruct {

    private final OrganisationPriorityRepository organisationPriorityRepository;

    private final UserRepository userRepository;
    private HashSet<String> categories = OrganisationPriorityCategory.categories;

    private final PasswordEncoder passwordEncoder;

    @jakarta.annotation.PostConstruct
    public void createAdmin() {
        Optional<User> admin = userRepository.findAdmin();
        if (admin.isEmpty()) {
            User adminDb = User.builder()
                    .username("admin@admin.com")
                    .password(passwordEncoder.encode("admin"))
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
