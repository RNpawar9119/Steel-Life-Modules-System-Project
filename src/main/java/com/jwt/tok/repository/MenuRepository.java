package com.jwt.tok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jwt.tok.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	
}
