package com.example.sacco.repository;

import com.example.sacco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // Ensure this method works correctly
    User findByUserIdAndPassword(String userId, String password);
}
