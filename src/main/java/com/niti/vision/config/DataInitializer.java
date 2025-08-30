package com.niti.vision.config;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.niti.vision.entity.Role;
import com.niti.vision.entity.User;
import com.niti.vision.repository.RoleRepository;
import com.niti.vision.repository.UserRepository;
@Configuration
public class DataInitializer {
	private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public DataInitializer(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
            ) {
this.userRepository = userRepository;
this.roleRepository = roleRepository;
this.passwordEncoder = passwordEncoder;
}
    @Bean
    CommandLineRunner init() {
    	return args -> {
            // Insert roles if missing
            Role adminRole = insertRoleIfNotExists("ADMIN");
            // Role userRole = insertRoleIfNotExists("ROLE_USER");

            // Insert default admin user
            if (userRepository.findByEmail("nitivisionglobal@gmail.com") == null) {
                User admin = User.builder()
                        .firstName("Niti")
                        .lastName("Vision")
                        .email("nitivisionglobal@gmail.com")
                        .password(passwordEncoder.encode("Trinity@123"))
                        .roles(Set.of(adminRole))
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(admin);
                System.out.println("✅ Default admin created: nitivisionglobal@gmail.com / Trinity@123");
            }
        };
    }

    private Role insertRoleIfNotExists(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(Role.builder().name(roleName).build()));
    }
}
