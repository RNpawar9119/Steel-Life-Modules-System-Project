package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    @Query("SELECT m FROM Material m WHERE " +
           "LOWER(m.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.typ) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.grp) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.des) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Material> searchMaterials(@Param("keyword") String keyword);
 
    
    @Query("SELECT m FROM Material m WHERE LOWER(m.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Material> searchByCode(@Param("keyword") String keyword);
    
    @Query("SELECT m FROM Material m WHERE m.typ LIKE 'Hardware%' AND LOWER(m.des) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Material> getHardware(@Param("keyword") String keyword);
    
    @Query("SELECT m FROM Material m WHERE " +
    	       "LOWER(m.typ) LIKE LOWER('hardw%') AND " +
    	       "LOWER(m.des) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    	List<Material> searchHardware(@Param("keyword") String keyword);
}