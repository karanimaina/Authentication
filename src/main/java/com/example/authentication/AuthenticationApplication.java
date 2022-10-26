package com.example.authentication;

import com.example.authentication.model.AppUser;
import com.example.authentication.model.Role;
import com.example.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@RequiredArgsConstructor
@Lazy
public class AuthenticationApplication implements CommandLineRunner {
private final UserService userService;
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userService.saveRole(new Role(null,"ROLE_USER"));
        userService.saveRole(new Role(null,"ROLE_MANAGER"));
        userService.saveRole(new Role(null,"ROLE_ADMIN"));
        userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));

        userService.saveUser(new AppUser(null,"John travolta","john","1234",new ArrayList<>()));
        userService.saveUser(new AppUser(null,"Will Smith","will","4567",new ArrayList<>()));
        userService.saveUser(new AppUser(null,"Jim Carry","jim","7890",new ArrayList<>()));
        userService.saveUser(new AppUser(null,"Felix maina","felix","3457",new ArrayList<>()));

        userService.addRoleToUser("john","ROLE_USER");
        userService.addRoleToUser("john","ROLE_ADMIN");
        userService.addRoleToUser("will","ROLE_MANAGER");
        userService.addRoleToUser("jim","ROLE_ADMIN");
        userService.addRoleToUser("felix","ROLE_USER");
        userService.addRoleToUser("felix","ROLE_ADMIN");
        userService.addRoleToUser("felix","ROLE_SUPER_ADMIN");

    }
}
