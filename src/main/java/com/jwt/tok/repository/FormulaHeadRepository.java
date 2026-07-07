package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.FormulaHead;

@Repository
public interface FormulaHeadRepository extends JpaRepository<FormulaHead, Long> {
	 List<FormulaHead> findByDescriptionContainingIgnoreCase(String keyword);
}