package com.jwt.tok.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.HNSMaster;
@Repository
public interface HNSMasterRepository extends JpaRepository <HNSMaster, Long> {
	Optional<HNSMaster> findByHnsCode(String hnsCode);
}
