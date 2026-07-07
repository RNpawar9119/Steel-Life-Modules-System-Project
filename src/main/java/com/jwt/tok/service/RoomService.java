package com.jwt.tok.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.Room;
import com.jwt.tok.repository.RoomRepository;

@Service
public class RoomService {

	private final RoomRepository roomRepository;

	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	// CREATE
	public Room create(Room room) {
		if (room.getRoomNo() == null || room.getRoomName() == null) {
			throw new ApiException("Room No and Room Name are required");
		}

		if (roomRepository.existsByRoomNo(room.getRoomNo())) {
			throw new ApiException("Room No already exists");
		}

		return roomRepository.save(room);
	}

	// UPDATE
	public Room update(Long id, Room updated) {
		Room room = roomRepository.findById(id).orElseThrow(() -> new ApiException("Room not found"));

		room.setRoomNo(updated.getRoomNo());
		room.setRoomName(updated.getRoomName());

		return roomRepository.save(room);
	}

	// GET ALL
	public List<Room> getAll() {
		return roomRepository.findAll();
	}

	// DELETE
	public void delete(Long id) {
		Room room = roomRepository.findById(id).orElseThrow(() -> new ApiException("Room not found"));
		roomRepository.delete(room);
	}

	public List<Room> search(String q) {
		if (q == null || q.trim().isEmpty()) {
			return roomRepository.findAll();
		}
		return roomRepository.findByRoomNameContainingIgnoreCaseOrRoomNoContainingIgnoreCase(q, q);
	}

	public Room getRoomById(Long id) {

		return roomRepository.findById(id).orElseThrow(() -> new ApiException("Room not found"));

	}
}
