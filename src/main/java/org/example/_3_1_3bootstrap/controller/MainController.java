package org.example._3_1_3bootstrap.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example._3_1_3bootstrap.model.Role;
import org.example._3_1_3bootstrap.model.User;
import org.example._3_1_3bootstrap.service.RoleService;
import org.example._3_1_3bootstrap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MainController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public MainController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping({"/","/admin"})
    @PreAuthorize("hasAuthority('ADMIN')")
    public String usersList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        List<Role> roles = roleService.findAllRoles();
        String mess = userService.findUserByNameWithRoles(authentication.getName()).getEmail() + " with roles: " + userService.findUserByNameWithRoles(authentication.getName()).getStringRoles();
        model.addAttribute("users", userService.findAllUsersWithRoles());
        model.addAttribute("message", mess);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "index";
    }

    @PostMapping("/admin/addUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveUser(@Valid @ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/editUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUser(@Valid @ModelAttribute("user") User user) {
        userService.editUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String showUserPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByNameWithRoles(authentication.getName());
        List<Role> roles = roleService.findAllRoles();
        String mess = userService.findUserByNameWithRoles(authentication.getName()).getEmail() + " with roles: " + userService.findUserByNameWithRoles(authentication.getName()).getStringRoles();
        model.addAttribute("user", user);
        model.addAttribute("message", mess);
        model.addAttribute("roles", roles);
        return "user";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            request.getSession().invalidate();
        }
        return "redirect:/";
    }
}
