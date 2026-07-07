package com.jwt.tok.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jwt.tok.model.ItemDiscription;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.ItemDiscriptionService;

@RestController
@RequestMapping("/api/item-description")
@CrossOrigin("*")
public class ItemDiscriptionController {

	private final ItemDiscriptionService service;

	public ItemDiscriptionController(ItemDiscriptionService service) {
		this.service = service;
	}

	@PostMapping(value = "/save", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<ItemDiscription>> save(@RequestParam String description,
			@RequestParam(required = false) MultipartFile pic) throws Exception {

		ItemDiscription saved = service.save(description, pic);

		return ResponseEntity.ok(ApiResponse.success("Saved successfully", saved));
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<ItemDiscription>>> getAll() {

		return ResponseEntity.ok(ApiResponse.success(service.getAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ItemDiscription>> getById(@PathVariable Long id) {

		return ResponseEntity.ok(ApiResponse.success(service.getById(id)));
	}

	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<ItemDiscription>> update(@PathVariable Long id, @RequestParam String description,
			@RequestParam(required = false) MultipartFile pic) throws Exception {

		ItemDiscription updated = service.update(id, description, pic);

		return ResponseEntity.ok(ApiResponse.success("Updated successfully", updated));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

		service.delete(id);

		return ResponseEntity.ok(ApiResponse.success("Deleted successfully", null));
	}
	
	// 🔥 AUTO SEARCH API
	@GetMapping("/auto-search")
	public ResponseEntity<ApiResponse<List<ItemDiscription>>> autoSearch(
	        @RequestParam(required = false) String keyword) {

	    return ResponseEntity.ok(
	            ApiResponse.success(service.search(keyword))
	    );
	}
}