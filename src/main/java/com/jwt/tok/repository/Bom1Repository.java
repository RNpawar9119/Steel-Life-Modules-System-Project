package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Bom1;

@Repository
public interface Bom1Repository extends JpaRepository<Bom1, Long> {

    // 🔍 Search like JSP (RID + DES)
    @Query("SELECT b FROM Bom1 b WHERE " +
           "LOWER(b.rid) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.des) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Bom1> searchBom(@org.springframework.data.repository.query.Param("keyword") String keyword);
    
    @Query("SELECT MAX(CAST(b.rid as long)) FROM Bom1 b")
    Long getMaxRid();
    
    
}