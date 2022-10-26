package com.example.authentication.service;

import com.example.authentication.model.AppUser;
import com.example.authentication.model.Role;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.util.List;

public interface UserInterface {
    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);
    AppUser getUser(String username);
    List<AppUser> getUser();
}
