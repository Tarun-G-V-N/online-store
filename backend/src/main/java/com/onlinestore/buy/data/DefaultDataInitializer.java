package com.onlinestore.buy.data;

import com.onlinestore.buy.entities.Role;
import com.onlinestore.buy.entities.User;
import com.onlinestore.buy.repositories.RoleRepository;
import com.onlinestore.buy.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DefaultDataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");
        createDefaultRoles(roles);
        createDefaultAdminIfNotExists();
    }

    private void createDefaultRoles(Set<String> roles) {
        roles.stream().filter(role -> Optional.ofNullable(roleRepository.findByName(role)).isEmpty())
                .map(Role :: new)
                .forEach(roleRepository :: save);
    }

    private void createDefaultAdminIfNotExists() {
        Role adminRole = Optional.ofNullable(roleRepository.findByName("ROLE_ADMIN"))
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        for(int i = 0; i < 3; i++) {
            if(userRepository.existsByEmail("admin" + i + "@gmail.com")) continue;
            User user = new User();
            user.setFirstname("Admin" + i);
            user.setLastname("ShopUser" + i);
            user.setEmail("admin" + i + "@gmail.com");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
        }
    }
}
