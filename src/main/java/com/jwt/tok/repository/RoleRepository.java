package com.jwt.tok.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Dealer;
import com.jwt.tok.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role); // Admin / Dealer

	//Optional<Role> findByName(String string);
}
