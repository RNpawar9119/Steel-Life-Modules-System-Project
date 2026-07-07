package com.jwt.tok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.SequenceProcess;
@Repository
public interface SequenceProcessRepository 
        extends JpaRepository<SequenceProcess, Long> {
}
