package com.bank.mtransfer.repository;

import com.bank.mtransfer.models.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Query("from User where username=:username")
    User loginUser(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
