package com.jwt.tok.repository;

import com.jwt.tok.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
	
	@Query("SELECT COALESCE(MAX(CAST(i.srno as int)), 0) FROM ImageData i WHERE i.docno = :docno")
	Integer findMaxSrnoByDocno(@Param("docno") String docno);

    List<ImageData> findByDocno(String docno);

    List<ImageData> findByDocnoAndLoc(String docno, String loc);
    Optional<ImageData> findByDocnoAndSrno(String docno, String srno);

    void deleteByDocnoAndSrno(String docno, String srno);
}