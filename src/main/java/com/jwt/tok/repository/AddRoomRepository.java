package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.AddRoomMS;

@Repository
public interface AddRoomRepository extends JpaRepository<AddRoomMS, Long> {

    List<AddRoomMS> findByDescriptionContainingIgnoreCase(String keyword);
    
    //List<AddRoomMS> findByDescriptionContainingIgnoreCase(String keyword);
}