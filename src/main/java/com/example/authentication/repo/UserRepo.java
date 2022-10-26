package com.example.authentication.repo;

import com.example.authentication.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);
}
