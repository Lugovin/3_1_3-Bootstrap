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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping({"/"})
    public String welcome() {
        return "/login";
    }

    @GetMapping({"/admin"})
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
    public String saveUser(Model model, @Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            result.getFieldErrors().forEach(error ->
                    errors.append(error.getField() + " - " + error.getDefaultMessage() + ", ")
            );
            List<Role> roles = roleService.findAllRoles();
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            model.addAttribute("errorMessage", errors);
            return "index";
        }
        if (user.getRoles().size() == 0) {
            List<Role> roles = roleService.findAllRoles();
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            model.addAttribute("errorMessage", "Укажите хотя бы одну роль!");
            return "index";
        }
        if (!userService.addUser(user)) {
            List<Role> roles = roleService.findAllRoles();
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            model.addAttribute("errorMessage", "Такой пользователь существует!");
            return "index";
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/deleteUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RedirectView deleteUser(@RequestParam Long id) {
        userService.deleteUserById(id);
        return new RedirectView("/admin");
    }

    @GetMapping("/admin/editUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUserForm(Model model, @RequestParam Long id) {
        User oldUser = userService.findUserByNameWithRoles(userService.findUserById(id).getName());
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("user", oldUser);
        model.addAttribute("roles", roles);
        //return "editUser";
        return "index";
    }

    @PostMapping("/admin/editUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUser(Model model, @Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            result.getFieldErrors().forEach(error ->
                    errors.append(error.getField() + " - " + error.getDefaultMessage() + ", ")
            );
            List<Role> roles = roleService.findAllRoles();
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            model.addAttribute("errorMessage", errors);
            return "editUser";
        }
        if (user.getRoles().size() == 0) {
            List<Role> roles = roleService.findAllRoles();
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            model.addAttribute("errorMessage", "Укажите хотя бы одну роль!");
            return "editUser";
        }
        userService.editUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String showUserPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByNameWithRoles(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("message", authentication.getName());
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
