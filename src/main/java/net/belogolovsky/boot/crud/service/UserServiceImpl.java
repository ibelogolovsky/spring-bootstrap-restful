package net.belogolovsky.boot.crud.service;

import net.belogolovsky.boot.crud.dao.RoleRepository;
import net.belogolovsky.boot.crud.dao.UserRepository;
import net.belogolovsky.boot.crud.model.Role;
import net.belogolovsky.boot.crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository users;

    @Autowired
    private RoleRepository roles;

    private PasswordEncoder encoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public UserServiceImpl() {}

    @Override
    public void save(User user) {
        save(user, users.findByEmail(user.getEmail()).isEmpty());
    }

    @Override
    public void save(User user, boolean encodePassword) {
        if (encodePassword) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        users.save(user);
    }

    @Override
    public void setPassword(User user, String password) {
        user.setPassword(encoder.encode(password));
    }

    @Override
    public User get(long id) {
        return users.findById(id).orElseGet(User::new);
    }

    @Override
    public boolean update(User user, long id) {
        if (users.findById(id).isPresent()) {
            user.setId(id);
            save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(long id) {
        if (users.existsById(id)) {
            users.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> listAll() {
        return (List<User>) users.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User %s not found", email))
        );
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(
                        role -> new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }

    @Override
    public void addRole(User user, String roleName) {
        Role role = roles.findByName(roleName).orElseGet(() -> new Role(roleName));
        user.getRoles().add(role);
        save(user);
    }
}
