package net.belogolovsky.boot.crud.security;

import net.belogolovsky.boot.crud.handler.LoginSuccessHandler;
import net.belogolovsky.boot.crud.service.UserService;
import net.belogolovsky.boot.crud.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug = true)
@ComponentScan("net.belogolovsky.boot.crud")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(SecurityConfig.class.getName());

    private UserDetailsService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    public void setUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Bean
    UserDetailsService getUserDetailsService() {
        return new UserServiceImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                .and().formLogin()
                    .loginPage("/login")
                    .usernameParameter("Email address")
                    .failureUrl("/login-error")
                    .successHandler(loginSuccessHandler)
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                    .csrf().disable()
                ;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(getPasswordEncoder());

        return authProvider;
    }

}
