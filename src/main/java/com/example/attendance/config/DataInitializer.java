package com.example.attendance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;
import com.example.attendance.security.SecurityConstants;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if not exists
        if (userRepository.findByUsername("admin@admin.com") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin@admin.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(SecurityConstants.ROLE_ADMIN);
            adminUser.setActive(true);
            userRepository.save(adminUser);
            System.out.println("Default admin user created");
        }
    }
}

