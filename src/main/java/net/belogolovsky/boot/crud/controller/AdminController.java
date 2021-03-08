package net.belogolovsky.boot.crud.controller;

import net.belogolovsky.boot.crud.model.Role;
import net.belogolovsky.boot.crud.model.User;
import net.belogolovsky.boot.crud.service.RoleService;
import net.belogolovsky.boot.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping()
    public String index(Model model, Principal principal,
                        @ModelAttribute("user") User user) {
        String adminName = principal.getName();
        User admin = userService.findByEmail(adminName)
                .orElseThrow(() -> new RuntimeException("Unable to find user by login: " + adminName));
        String roles = admin.getRoles().stream()
                .map(Role::toString).collect(Collectors.joining(" "));
        model.addAttribute("service", userService);
        model.addAttribute("admin", admin);
        model.addAttribute("roles", roles);
        model.addAttribute("users", userService.listAll());
        model.addAttribute("allRoles", roleService.listAll());
        return "admin";
    }

    @PostMapping
    public String create(@ModelAttribute("user") User user,
                         @RequestParam(value = "roles") Role[] rolesArray) {
        User newUser = new User(user);
        for (Role role : rolesArray) {
            userService.addRole(newUser, role.getName());
        }
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam(value = "roles", required = false) Role[] rolesArray,
                         @PathVariable("id") long id) {
        user.setId(id);
        User dbUser = userService.get(id);
        boolean passwordUpdated = true;
        if (user.getPassword().equals("")) {
            user.setPassword(dbUser.getPassword());
            passwordUpdated = false;
        }
        user.setRoles(new HashSet<>());
        userService.save(user, passwordUpdated);
        if (rolesArray != null) {
            for (Role role : rolesArray) {
                userService.addRole(user, role.getName());
            }
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.remove(id);
        return "redirect:/admin";
    }
}
