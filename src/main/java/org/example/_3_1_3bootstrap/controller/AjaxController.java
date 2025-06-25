package org.example._3_1_3bootstrap.controller;

import org.example._3_1_3bootstrap.model.User;
import org.example._3_1_3bootstrap.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/ajax")
public class AjaxController {

    private final UserService userService;

    public AjaxController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-email")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String email) {
        boolean isUnique = (userService.findUserByEmailWithRoles(email) == null);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isUnique", isUnique);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findUserById(@PathVariable long userId) {
        return userService.findUserByIdWithRoles(userId);
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteUserById(@RequestParam Long id) {
        userService.deleteUserById(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDelete", true);
        return ResponseEntity.ok(response);

    }
}
