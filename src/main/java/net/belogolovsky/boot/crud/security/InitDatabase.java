package net.belogolovsky.boot.crud.security;

import net.belogolovsky.boot.crud.model.User;
import net.belogolovsky.boot.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase {

    private UserService userService;

    @Autowired
    public InitDatabase(UserService userService) {
        User jsmith = new User("John", "Smith", 15,
                "jsmith@gmail.com", "1234");
        User admin = new User("Igor", "Belogolovsky", 38,
                "igor@belogolovsky.net", "admin");
        userService.addRole(jsmith, "ROLE_USER");
        userService.addRole(admin, "ROLE_ADMIN");
        userService.addRole(admin, "ROLE_USER");

    }



}
