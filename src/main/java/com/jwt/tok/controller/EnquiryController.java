package com.jwt.tok.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.tok.model.Enquiry;
import com.jwt.tok.repository.DealerRepository;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.EnquiryService;
import com.jwt.tok.util.JwtUtil;

@RestController
@RequestMapping("/api/enquiry")
@CrossOrigin(origins = "*")
public class EnquiryController {

	@Autowired
	private JwtUtil jwtUtil;

	private final EnquiryService enquiryService;
	private final ObjectMapper mapper;
	@Autowired
	private DealerRepository dealerRepository;

	public EnquiryController(EnquiryService enquiryService, ObjectMapper mapper) {
		this.enquiryService = enquiryService;
		this.mapper = mapper;
	}

	@PreAuthorize("hasRole('Dealer')")
	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<Enquiry>> create(@RequestPart("enquiry") String enquiryJson,
			@RequestPart(value = "enquiryForm", required = false) MultipartFile enquiryForm,
			@RequestPart(value = "finalizationReport", required = false) MultipartFile finalizationReport,
			@RequestPart(value = "actualDimension", required = false) MultipartFile actualDimension,
			@RequestPart(value = "design2D3D", required = false) MultipartFile design2D3D, HttpServletRequest request)
			throws IOException {

		Enquiry enquiry = mapper.readValue(enquiryJson, Enquiry.class);

		String token = request.getHeader("Authorization").substring(7);
		String dealerCode = jwtUtil.extractDealerCode(token);

		enquiry.setDealerCode(dealerCode); // ✅ AUTO SET

		Enquiry saved = enquiryService.create(enquiry, enquiryForm, finalizationReport, actualDimension, design2D3D);

		return ResponseEntity.status(201).body(ApiResponse.success("Enquiry created successfully", saved));
	}

	@PreAuthorize("hasAnyRole('Dealer')")
	@GetMapping("/EnquiriesGetall")
	public ResponseEntity<ApiResponse<List<Enquiry>>> getAll(HttpServletRequest request) {

		String token = request.getHeader("Authorization").substring(7);

		String role = jwtUtil.extractRole(token);
		String dealerCode = jwtUtil.extractDealerCode(token);

		return ResponseEntity
				.ok(ApiResponse.success("Enquiries fetched successfully", enquiryService.getAll(role, dealerCode)));
	}

//	@PreAuthorize("hasAnyRole('Dealer')")
//	@GetMapping("/getbyid/{enquiryId}")
//	public ResponseEntity<ApiResponse<Enquiry>> getById(@PathVariable String enquiryId) {
//
//	    return ResponseEntity.ok(
//	        ApiResponse.success("Enquiry fetched successfully",
//	            enquiryService.getById(enquiryId))
//	    );
//	}

	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<ApiResponse<Enquiry>> getById(@PathVariable Long id,
	        HttpServletRequest request) {

	    String token = request.getHeader("Authorization").substring(7);
	    String role = jwtUtil.extractRole(token);
	    String dealerCode = jwtUtil.extractDealerCode(token);

	    Enquiry enquiry = enquiryService.getByIdSecure(id, role, dealerCode);

	    return ResponseEntity.ok(ApiResponse.success("Enquiry fetched successfully", enquiry));
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<Enquiry>> update(
	        @PathVariable Long id,
	        @RequestPart("enquiry") String enquiryJson,
	        @RequestPart(value = "enquiryForm", required = false) MultipartFile enquiryForm,
	        @RequestPart(value = "finalizationReport", required = false) MultipartFile finalizationReport,
	        @RequestPart(value = "actualDimension", required = false) MultipartFile actualDimension,
	        @RequestPart(value = "design2D3D", required = false) MultipartFile design2D3D,
	        HttpServletRequest request) throws IOException {

	    String token = request.getHeader("Authorization").substring(7);
	    String role = jwtUtil.extractRole(token);
	    String dealerCode = jwtUtil.extractDealerCode(token);

	    Enquiry updated = enquiryService.updateSecure(
	            id,
	            enquiryJson,
	            enquiryForm,
	            finalizationReport,
	            actualDimension,
	            design2D3D,
	            role,
	            dealerCode);

	    return ResponseEntity.ok(ApiResponse.success("Enquiry updated successfully", updated));
	}
	// DELETE
//	@PreAuthorize("hasAnyRole('Dealer')")
//	@DeleteMapping("/delete/{enquiryId}")
//	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable String enquiryId) {
//
//		enquiryService.delete(enquiryId);
//
//		return ResponseEntity.ok(ApiResponse.success("Enquiry deleted successfully"));
//	}

	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(
	        @PathVariable Long id,
	        HttpServletRequest request) {

	    String token = request.getHeader("Authorization").substring(7);
	    String role = jwtUtil.extractRole(token);
	    String dealerCode = jwtUtil.extractDealerCode(token);

	    enquiryService.deleteSecure(id, role, dealerCode);

	    return ResponseEntity.ok(ApiResponse.success("Enquiry deleted successfully"));
	}
	
	
	@PreAuthorize("hasAnyRole('Dealer')")
	@GetMapping("/count")
	public ResponseEntity<ApiResponse<Long>> getOrderCount(HttpServletRequest request) {

		String token = request.getHeader("Authorization").substring(7);

		String role = jwtUtil.extractRole(token);
		String dealerCode = jwtUtil.extractDealerCode(token);

		long count = enquiryService.getOrderCount(role, dealerCode);

		return ResponseEntity.ok(ApiResponse.success("Enquiry count fetched", count));
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@PutMapping("/mark-ordered/{id}")
	public ResponseEntity<ApiResponse<Object>> markOrdered(@PathVariable Long id) {

	    enquiryService.markEnquiryAsOrdered(id);

	    return ResponseEntity.ok(ApiResponse.success("Enquiry marked as ordered"));
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@GetMapping("/reportall")
	public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAll1(HttpServletRequest request) {

		String token = request.getHeader("Authorization").substring(7);

		String role = jwtUtil.extractRole(token);
		String dealerCode = jwtUtil.extractDealerCode(token);

		return ResponseEntity.ok(ApiResponse.success("Enquiries fetched successfully",
				enquiryService.getAll1WithDealerName(role, dealerCode)));
	}

	@PreAuthorize("hasAnyRole('Dealer','Admin')")
	@GetMapping("/assigned")
	public ResponseEntity<ApiResponse<List<Enquiry>>> getAssigned(HttpServletRequest request) {

		String token = request.getHeader("Authorization").substring(7);

		String role = jwtUtil.extractRole(token);
		String dealerCode = jwtUtil.extractDealerCode(token);

		return ResponseEntity.ok(ApiResponse.success("Assigned enquiries fetched successfully",
				enquiryService.getAllAssigned(role, dealerCode)));
	}
	
	
	@GetMapping("/search-by-date")
	public ResponseEntity<ApiResponse<List<Map<String, Object>>>> searchByDate(
	        @RequestParam String fromDate,
	        @RequestParam String toDate,
	        HttpServletRequest request) {

	    String token = request.getHeader("Authorization").substring(7);
	    String role = jwtUtil.extractRole(token);
	    String dealerCode = jwtUtil.extractDealerCode(token);

	    List<Map<String, Object>> list = enquiryService.getEnquiriesByDateRangeWithDealer(
	            LocalDate.parse(fromDate),
	            LocalDate.parse(toDate),
	            role,
	            dealerCode
	    );

	    return ResponseEntity.ok(
	            ApiResponse.success("Filtered enquiries", list)
	    );
	}
	
	
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@GetMapping("/assigned/search-by-date")
	public ResponseEntity<ApiResponse<List<Enquiry>>> getAssignedByDate(
	        @RequestParam String fromDate,
	        @RequestParam String toDate,
	        HttpServletRequest request
	) {

	    String token = request.getHeader("Authorization").substring(7);
	    String role = jwtUtil.extractRole(token);
	    String dealerCode = jwtUtil.extractDealerCode(token);

	    List<Enquiry> list = enquiryService.getAssignedByDateRange(
	            LocalDate.parse(fromDate),
	            LocalDate.parse(toDate),
	            role,
	            dealerCode
	    );

	    return ResponseEntity.ok(
	            ApiResponse.success("Assigned enquiries filtered", list)
	    );
	}
}
