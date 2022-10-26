package com.example.authentication.service;

import com.example.authentication.model.AppUser;
import com.example.authentication.model.Role;
import com.example.authentication.repo.RoleRepo;
import com.example.authentication.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService implements  UserInterface, UserDetailsService {
    private final UserRepo userRepo;
    private  final RoleRepo roleRepo;
    private  final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException("user does not exist");
        }
        Collection<SimpleGrantedAuthority>authorities = new ArrayList<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(user.getUsername(),user.getPassword(),authorities);
    }
    @Override
    public AppUser saveUser(AppUser user) {
        log.info("saving user"+user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving role"+role);
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("saving user with username "+username +"  into role with role name"+ roleName);
     AppUser user = userRepo.findByUsername(username);
     Role role = roleRepo.findByName(roleName);
     user.getRole().add(role);
    }

    @Override
    public AppUser getUser(String username) {
        log.info("finding  users");
        return userRepo.findByUsername(username);
    }

    @Override
    public List<AppUser> getUser() {
         log.info("fetching all users");
        return userRepo.findAll();
    }



}
