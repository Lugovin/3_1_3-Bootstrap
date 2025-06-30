package org.example._3_1_3bootstrap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example._3_1_3bootstrap.model.User;
import org.example._3_1_3bootstrap.service.RoleService;
import org.example._3_1_3bootstrap.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AjaxController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public AjaxController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
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
    public ResponseEntity<String> findUserById(@PathVariable long userId) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(userService.findUserByIdWithRoles(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(jsonString);
        return ResponseEntity.ok(jsonString);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> findAllUsers() {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(userService.findAllUsersWithRoles());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonString);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> findAllRoles() {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(roleService.findAllRoles());
            System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonString);
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> deleteUserById(@RequestParam Long id) {
        User user = userService.findUserByIdWithRoles(id);
        userService.deleteUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/editUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.editUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return ResponseEntity.ok(user);
    }
}
