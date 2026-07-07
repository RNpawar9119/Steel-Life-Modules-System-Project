package com.jwt.tok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.QuoMaster;

@Repository
public interface QuoMasterRepository extends JpaRepository<QuoMaster, Long> {
}