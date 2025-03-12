package com.demotake5.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.demotake5.entity.User;
import com.demotake5.exception.ValidationException;
import com.demotake5.repository.UserRepository;
import com.demotake5.service.UserService;

@SpringBootTest
public class UserServiceParameterizedTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        // Delete all users before each test to avoid duplicate username conflicts
        userRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({
            "user, adminpassword",
            "user1, password1",
            "user2, password2"
    })
    public void testRegisterUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        User registeredUser = userService.registerUser(user);
        assertNotNull(registeredUser);
        assertEquals(username, registeredUser.getUsername());
    }

    @ParameterizedTest
    @CsvSource({
            "user, adminpassword",
            "user1, password1"
    })
    public void testRegisterUser_DuplicateUsername(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // Register the user once
        userService.registerUser(user);

        // Attempt to register the same user again
        assertThrows(ValidationException.class, () -> userService.registerUser(user));
    }
}