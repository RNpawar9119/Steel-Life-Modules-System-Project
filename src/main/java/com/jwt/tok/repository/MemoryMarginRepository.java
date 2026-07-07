package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.MemoryMargin;

@Repository
public interface MemoryMarginRepository extends JpaRepository<MemoryMargin, Long> {

    List<MemoryMargin> findByTypeContainingIgnoreCase(String type);

}