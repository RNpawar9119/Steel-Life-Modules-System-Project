package com.jwt.tok.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jwt.tok.model.OrderItem;
import com.jwt.tok.repository.OrderItemRepository;
import com.jwt.tok.service.DealerService;
import com.jwt.tok.service.OrderItemService;
import com.jwt.tok.util.JwtUtil;

@RestController
@RequestMapping("/api/order-items")
@CrossOrigin("*")
public class OrderItemController {

	private final OrderItemService service;
	private final OrderItemRepository itemRepo;
	private final DealerService dealerService;
	private final JwtUtil jwtUtil;

	public OrderItemController(OrderItemService service, OrderItemRepository itemRepo, DealerService dealerService,
			JwtUtil jwtUtil) {
		this.service = service;
		this.itemRepo = itemRepo;
		this.dealerService = dealerService;
		this.jwtUtil = jwtUtil;
	}
	// -----------------------------------------------------------------------------
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@PostMapping(value = "/upload/{orderId}", consumes = "multipart/form-data")
	public ResponseEntity<OrderItem> upload(@PathVariable Long orderId, @RequestParam String room,
			@RequestParam(required = false) Long sequenceId, // ⭐ ADD THIS
			@RequestParam String product, @RequestParam(required = false) MultipartFile doc1,
			@RequestParam(required = false) MultipartFile doc2, @RequestParam(required = false) MultipartFile doc3,
			@RequestParam(required = false) MultipartFile doc4, @RequestParam(required = false) MultipartFile doc5)
			throws Exception {

		OrderItem savedItem = service.saveItem(orderId, room, product, sequenceId, doc1, doc2, doc3, doc4, doc5);
		return ResponseEntity.ok(savedItem);
	}

//----------------------------------------------------------------------
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<OrderItem> updateItem(@PathVariable Long id, @RequestParam String room,
			@RequestParam(required = false) Long sequenceId, // ⭐ ADD
			@RequestParam String product, @RequestParam(required = false) MultipartFile doc1,
			@RequestParam(required = false) MultipartFile doc2, @RequestParam(required = false) MultipartFile doc3,
			@RequestParam(required = false) MultipartFile doc4, @RequestParam(required = false) MultipartFile doc5)
			throws Exception {

		OrderItem updated = service.updateItem(id, room, product, sequenceId, doc1, doc2, doc3, doc4, doc5);
		return ResponseEntity.ok(updated);
	}

	// ---------------------------------------------------------------------
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteOrderItem(@PathVariable Long id) {
		service.deleteOrderItem(id);
		return ResponseEntity.ok("Order item deleted successfully");
	}

	// --------------------------------------------------------------------------
	// Assign Dealer
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@PutMapping("/assign/{itemId}")
	public ResponseEntity<String> assignDealer(@PathVariable Long itemId, @RequestParam String dealerCode) {

		service.assignDealer(itemId, dealerCode);
		return ResponseEntity.ok("Dealer assigned successfully");
	}

	// --------------------------------------------------------------------------
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@GetMapping("/assigned-list")
	public ResponseEntity<List<Map<String, Object>>> getAssignedOrders(@RequestHeader("Authorization") String token) {

		String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

		String username = jwtUtil.extractUsername(jwt);
		String role = jwtUtil.extractRole(jwt); // Admin / Dealer

		List<Object[]> data;

		if ("Admin".equalsIgnoreCase(role)) {
			data = itemRepo.findAssignedOrders();
		} else if ("Dealer".equalsIgnoreCase(role)) {
			data = itemRepo.findAssignedOrdersByDealerUsername(username);
		} else {
			return ResponseEntity.status(403).build();
		}

		List<Map<String, Object>> response = data.stream().map(obj -> {
			Map<String, Object> map = new HashMap<>();
			map.put("itemId", obj[0]);
			map.put("roomNo", obj[1]);
			map.put("product", obj[2]);
			map.put("dealerName", obj[3]);
			map.put("dealerCode", obj[4]);
			return map;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}
	@GetMapping("/barcode/pages/{itemId}")
	public ResponseEntity<?> getBarcodePages(@PathVariable Long itemId) {

	    OrderItem item = itemRepo.findById(itemId)
	            .orElseThrow(() -> new RuntimeException("Item not found"));

	    String pdfPath = item.getDoc5();

	    List<Map<String, Object>> pages = service.getBarcodePages(pdfPath);

	    return ResponseEntity.ok(pages);
	}
}