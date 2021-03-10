package net.belogolovsky.boot.crud.controller;

import net.belogolovsky.boot.crud.model.Role;
import net.belogolovsky.boot.crud.model.User;
import net.belogolovsky.boot.crud.service.RoleService;
import net.belogolovsky.boot.crud.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;

    private final RoleService roleService;

    public ApiController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User user) {
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<?> read() {
        final List<User> users = userService.listAll();
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/users/{id}", produces = "application/json")
    public ResponseEntity<?> read(@PathVariable(name = "id") long id) {
        final User user = userService.get(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/users/{id}", consumes = "application/json")
    public ResponseEntity<?> update(@PathVariable(name = "id") long id,
                                    @RequestBody User user) {
        Set<Role> roles =  user.getRoles().stream()
                .map(x -> roleService.get(Long.parseLong(x.getName())))
                .collect(Collectors.toSet());
        if (userService.get(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else {
            user.setId(id);
            User dbUser = userService.get(id);
            boolean passwordUpdated = true;
            if (user.getPassword().equals("")) {
                user.setPassword(dbUser.getPassword());
                passwordUpdated = false;
            }
            user.setRoles(roles);
            userService.save(user, passwordUpdated);
//            if (rolesArray != null) {
//                for (Role role : rolesArray) {
//                    userService.addRole(user, role.getName());
//                }
//            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        final boolean deleted = userService.remove(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}


