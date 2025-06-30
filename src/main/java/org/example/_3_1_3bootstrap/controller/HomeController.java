package org.example._3_1_3bootstrap.controller;

import org.example._3_1_3bootstrap.model.User;
import org.example._3_1_3bootstrap.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/admin"})
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView adminPage() {
        return new ModelAndView("index");
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ModelAndView userPage() {
        return new ModelAndView("user");
    }

    @GetMapping("/auth")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<User> getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User user = userService.findUserByEmailWithRoles(principal.getEmail());
        return ResponseEntity.ok(user);
    }
}
