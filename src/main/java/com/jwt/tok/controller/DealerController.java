package com.jwt.tok.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

import com.jwt.tok.model.Dealer;
import com.jwt.tok.repository.DealerRepository;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.DealerService;

@RestController
@RequestMapping("/api/dealer")
@CrossOrigin(origins = "*")
public class DealerController {
	private final DealerRepository dealerRepo;

	private final DealerService service;

	public DealerController(DealerService service, DealerRepository dealerRepo) {
		this.service = service;
		this.dealerRepo = dealerRepo;
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Dealer>> create(@RequestBody Dealer dealer) {

		return ResponseEntity.status(201)
				.body(ApiResponse.success("Dealer created successfully", service.create(dealer)));
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<Dealer>>> getAll() {
		return ResponseEntity.ok(ApiResponse.success("Dealers fetched successfully", service.getAll()));
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Dealer>> getById(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success("Dealer fetched successfully", service.getById(id)));
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ApiResponse<Dealer>> update(@PathVariable Long id, @RequestBody Dealer dealer) {

		return ResponseEntity.ok(ApiResponse.success("Dealer updated successfully", service.update(id, dealer)));
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.ok(ApiResponse.success("Dealer deleted successfully"));
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> searchDealer(@RequestParam String name) {

		List<Dealer> dealers = service.searchByNamePrefix(name);

		Map<String, Object> response = new HashMap<>();
		response.put("data", dealers);

		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@GetMapping("/next-code")
	public ResponseEntity<ApiResponse<String>> getNextDealerCode() {
		return ResponseEntity.ok(ApiResponse.success("Next dealer code", service.previewDealerCode()));
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@PutMapping("/toggle-status/{id}")
	public ResponseEntity<ApiResponse<String>> toggleDealerStatus(@PathVariable Long id) {

		service.toggleStatus(id);
		return ResponseEntity.ok(ApiResponse.success("Dealer status updated"));
	}
	@PreAuthorize("hasRole('Dealer')")
	@PutMapping("/change-credentials")
	public ResponseEntity<ApiResponse<String>> changeCredentials(
	        @RequestBody Dealer dealer,
	        Authentication authentication) {

	    String username = authentication.getName(); // logged-in user

	    service.changeCredentials(username, dealer);

	    return ResponseEntity.ok(ApiResponse.success("Credentials updated successfully"));
	}
	
}
