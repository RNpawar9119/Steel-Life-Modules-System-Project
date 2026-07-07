package com.jwt.tok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.TaxMaster;

@Repository
 public interface TaxMasterRepository extends JpaRepository <TaxMaster, Long>{
	 
 }