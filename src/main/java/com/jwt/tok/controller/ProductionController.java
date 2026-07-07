package com.jwt.tok.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tok.model.Orders;
import com.jwt.tok.service.ProductionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductionController {

	private final ProductionService service;

	@PostMapping("/start")
	public ResponseEntity<?> start(@RequestParam Long orderItemId, @RequestParam Long processId) {

		return ResponseEntity.ok(service.startProcess(orderItemId, processId));
	}

	@PostMapping("/stop")
	public ResponseEntity<?> stop(@RequestParam Long orderItemId, @RequestParam Long processId) {

		return ResponseEntity.ok(service.stopProcess(orderItemId, processId));
	}

	@PostMapping("/complete")
	public ResponseEntity<?> complete(@RequestParam Long orderItemId, @RequestParam Long processId) {

		return ResponseEntity.ok(service.completeProcess(orderItemId, processId));
	}

	@GetMapping("/status")
	public ResponseEntity<?> getStatus(@RequestParam Long orderItemId) {

		return ResponseEntity.ok(service.getItemProgress(orderItemId));
	}

	@GetMapping("/orders/production/completed")
	public ResponseEntity<?> getCompletedOrders() {

		List<Orders> completed = service.getCompletedProductionOrders();
		Map<String, Object> response = new HashMap<>();
		response.put("data", completed);

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/history")
	public ResponseEntity<?> getProcessHistory(
	        @RequestParam Long orderItemId,
	        @RequestParam Long processId) {

	    return ResponseEntity.ok(
	        service.getProcessHistory(orderItemId, processId)
	    );
	}
	
	@GetMapping("/orders/production/running")
	public ResponseEntity<?> getRunningOrdersForAdmin() {

	    List<Orders> runningOrders = service.getRunningOrdersForAdmin();

	    Map<String, Object> response = new HashMap<>();
	    response.put("data", runningOrders);

	    return ResponseEntity.ok(response);
	}
}