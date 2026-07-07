package com.jwt.tok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jwt.tok.model.QuantitySurvey;

public interface QuantitySurveyRepository extends JpaRepository<QuantitySurvey, Long> {
	@Query("SELECT MAX(CAST(q.surveyNo as long)) FROM QuantitySurvey q")
	Long findMaxSurveyNo();
	
	boolean existsBySurveyNo(String surveyNo);
}