package com.demotake5.config;

import com.demotake5.entity.RoleType;
import com.demotake5.entity.User;
import com.demotake5.repository.UserRepository;
import com.demotake5.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        // Check if the admin user already exists
        if (userRepository.findByUsername("admin") == null) {
            // Create the admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("adminpassword"); // Replace with a secure password

            // Register the admin user (assigns the BASIC role)
            userService.registerUser(admin);

            // Assign the ADMIN role explicitly
            userService.assignRoleToUser(admin.getId(), RoleType.ADMIN);

            System.out.println("Admin user created successfully.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}