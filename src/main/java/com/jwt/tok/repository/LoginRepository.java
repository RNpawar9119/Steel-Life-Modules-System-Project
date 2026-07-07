package com.jwt.tok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
	Login findByUsername(String username);
	
}
