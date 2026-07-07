package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    //boolean existsByRoomNo(String roomNo);
    //search roomno
    List<Room> findByRoomNoContainingIgnoreCase(String roomNo);
    
    List<Room> findByRoomNameContainingIgnoreCase(String roomName);
  
    
    boolean existsByRoomNo(String roomNo);

    List<Room> findByRoomNameContainingIgnoreCaseOrRoomNoContainingIgnoreCase(
            String roomName,
            String roomNo
    );
}
