package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.ItemDiscription;

@Repository
public interface ItemDiscriptionRepository extends JpaRepository<ItemDiscription, Long> {
	List<ItemDiscription> findByDescriptionContainingIgnoreCase(String keyword);

}