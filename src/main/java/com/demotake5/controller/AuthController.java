package com.demotake5.controller;

import com.demotake5.entity.User;
import com.demotake5.exception.ValidationException;
import com.demotake5.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model, String error, String logout) {
        // Display logout success message
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        // Display login error message
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }

        return "auth/login"; // Renders login.html
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register"; // Renders register.html
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        // Handle validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Please correct the errors below.");
            return "auth/register";
        }

        try {
            userService.registerUser(user);
            model.addAttribute("successMessage", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}