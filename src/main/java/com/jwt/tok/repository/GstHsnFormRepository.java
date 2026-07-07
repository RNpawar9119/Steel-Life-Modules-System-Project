package com.jwt.tok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.GstHsnForm;

@Repository
public interface GstHsnFormRepository extends JpaRepository<GstHsnForm, Long> {

    // 🔥 last CODE find करायला
    GstHsnForm findTopByOrderByIdDesc();
}