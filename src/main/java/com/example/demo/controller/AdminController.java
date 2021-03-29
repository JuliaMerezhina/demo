package com.example.demo.controller;


import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String adminPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedUser", userService.getUserByUsername(auth.getName()));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());

        return "admin";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute User user,
                             @RequestParam("role") String[] role) {
        Set<Role> roleSet = new HashSet<>();
        for (String userRole : role) {
            roleSet.add(roleService.getRoleByName(userRole));
        }
        user.setRoles(roleSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute User user,
                         @RequestParam("role") String[] roles) {
        User updUser = userService.getUserById(user.getId());
        updUser.setUsername(user.getUsername());
        updUser.setEmail(user.getEmail());
        updUser.setAge(user.getAge());
        updUser.setLastname(user.getLastname());

        if (!user.getPassword().equals("")) {
            updUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            roleSet.add(roleService.getRoleByName(role));
        }
        updUser.setRoles(roleSet);

        userService.saveUser(updUser);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}