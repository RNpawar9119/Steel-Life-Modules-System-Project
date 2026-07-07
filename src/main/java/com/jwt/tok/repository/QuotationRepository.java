package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.tok.model.QuotationMaster;

public interface QuotationRepository extends JpaRepository<QuotationMaster, Long> {
	List<QuotationMaster> findByDealerCode(String dealerCode);
	boolean existsByEnquiryId(Long enquiryId);

}
