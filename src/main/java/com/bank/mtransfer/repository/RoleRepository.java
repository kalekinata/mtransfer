package com.bank.mtransfer.repository;

import com.bank.mtransfer.models.ERole;
import com.bank.mtransfer.models.db.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
