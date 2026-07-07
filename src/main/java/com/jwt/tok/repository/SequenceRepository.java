package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Sequence;

@Repository
public interface SequenceRepository extends JpaRepository<Sequence, Long> {
	List<Sequence> findBySequenceNameContainingIgnoreCase(String sequenceName);
}
