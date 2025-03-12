package com.demotake5.service;

import com.demotake5.entity.Role;
import com.demotake5.entity.RoleType;
import com.demotake5.entity.User;
import com.demotake5.exception.ValidationException;
import com.demotake5.repository.RoleRepository;
import com.demotake5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public User registerUser(User user) {
        // Check if the username already exists
        if (usernameExists(user.getUsername())) {
            throw new ValidationException("Username already exists: " + user.getUsername());
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign the default BASIC role
        Role basicRole = roleRepository.findByName(RoleType.BASIC)
                .orElseThrow(() -> new ValidationException("Default role not found: BASIC"));
        user.setRoles(Set.of(basicRole)); // Assign the BASIC role

        return userRepository.save(user);
    }

    public void assignRoleToUser(Long userId, RoleType roleType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new ValidationException("Invalid role: " + roleType));

        user.setRoles(Set.of(role)); // Replace existing roles with the new role
        userRepository.save(user);
    }

    // Add this method to retrieve all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}