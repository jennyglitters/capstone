package com.demotake5.controller;

import com.demotake5.entity.RoleType;
import com.demotake5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user-management")
public class UserManagementController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String userManagementPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-management";
    }

    @PostMapping("/assign-role")
    public String assignRoleToUser(@RequestParam Long userId, @RequestParam RoleType role) {
        userService.assignRoleToUser(userId, role);
        return "redirect:/user-management";
    }
}