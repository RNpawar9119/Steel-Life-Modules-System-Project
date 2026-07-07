package com.jwt.tok.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tok.model.Room;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Room>> create(@RequestBody Room room) {
		return ResponseEntity.ok(ApiResponse.success("Room created", roomService.create(room)));
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ApiResponse<Room>> update(@PathVariable Long id, @RequestBody Room room) {

		return ResponseEntity.ok(ApiResponse.success("Room updated", roomService.update(id, room)));
	}

	// @PreAuthorize("hasRole('Admin')")
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<Room>>> getAll() {
		return ResponseEntity.ok(ApiResponse.success("Rooms fetched", roomService.getAll()));
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
		roomService.delete(id);
		return ResponseEntity.ok(ApiResponse.success("Room deleted"));
	}
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<Room>>> searchRoom(
	        @RequestParam String q
	) {
	    return ResponseEntity.ok(
	        ApiResponse.success("Rooms fetched", roomService.search(q))
	    );
	}
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<ApiResponse<Room>> getRoomById(@PathVariable Long id) {

	    Room room = roomService.getRoomById(id);

	    return ResponseEntity.ok(
	        ApiResponse.success("Room fetched", room)
	    );
	}
}
