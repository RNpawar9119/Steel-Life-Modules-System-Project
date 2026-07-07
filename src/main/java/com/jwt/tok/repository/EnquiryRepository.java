package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Enquiry;
import com.jwt.tok.model.Orders;

import java.time.LocalDate;


@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    List<Enquiry> findByStatus(String status);

    List<Enquiry> findByDealerCode(String dealerCode);

    long countByStatus(String status);

    long countByDealerCodeAndStatus(String dealerCode, String status);

    List<Enquiry> findByStatusAndDealerCode(String status, String dealerCode);

    long countByEnquiryIdStartingWith(String prefix);
    
   //form date to date
    List<Enquiry> findByDateBetween(LocalDate fromDate, LocalDate toDate);
    
    List<Enquiry> findByStatusAndDateBetween(String status,LocalDate fromDate,LocalDate toDate);
    
}