package net.belogolovsky.boot.crud.controller;

import net.belogolovsky.boot.crud.model.User;
import net.belogolovsky.boot.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("user")
    public String user(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Unable to find user by login: " + principal.getName()));
        model.addAttribute("user", user);
        return "user";
    }
}
