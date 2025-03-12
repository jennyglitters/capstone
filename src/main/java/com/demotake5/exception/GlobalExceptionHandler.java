package com.demotake5.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public String handleValidationException(ValidationException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error"; // Render a custom error page
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error"; // Render a custom error page
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model) {
        model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        return "error"; // Render a custom error page
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e, Model model) {
        model.addAttribute("error", "Database error: " + e.getMessage());
        return "error";
    }
}