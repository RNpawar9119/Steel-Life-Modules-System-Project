package com.jwt.tok.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jwt.tok.model.Product;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	/* ================= CREATE PRODUCT ================= */
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {

		Product saved = service.createProduct(product);
		return ResponseEntity.status(201).body(ApiResponse.success("Product created", saved));
	}

	/* ================= UPLOAD IMAGES ================= */
	@PreAuthorize("hasAnyRole('Admin')")
	@PostMapping(value = "/{id}/upload-images", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<Product>> uploadImages(@PathVariable Long id,
			@RequestPart(value = "image1", required = false) MultipartFile image1,
			@RequestPart(value = "image2", required = false) MultipartFile image2,
			@RequestPart(value = "image3", required = false) MultipartFile image3) throws IOException {

		Product updated = service.uploadImages(id, image1, image2, image3);
		return ResponseEntity.ok(ApiResponse.success("Images uploaded", updated));
	}

	/* ================= GET BY ID ================= */
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> getById(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success("Product fetched", service.getById(id)));
	}

	/* ================= GET ALL ================= */
	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	public ResponseEntity<ApiResponse<List<Product>>> getAll() {
		return ResponseEntity.ok(ApiResponse.success("Products fetched", service.getAll()));
	}

	/* ================= UPDATE ================= */
	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id,
			@RequestPart("product") String productJson,
			@RequestPart(value = "image1", required = false) MultipartFile image1,
			@RequestPart(value = "image2", required = false) MultipartFile image2,
			@RequestPart(value = "image3", required = false) MultipartFile image3) throws IOException {

		Product updated = service.updateProduct(id, productJson, image1, image2, image3);
		return ResponseEntity.ok(ApiResponse.success("Product updated", updated));
	}

	/* ================= DELETE ================= */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
		service.deleteProduct(id);
		return ResponseEntity.ok(ApiResponse.success("Deleted", "OK"));
	}

	/* ================= SEARCH ================= */
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<Product>>> search(@RequestParam(required = false) String productCode) {

		return ResponseEntity.ok(ApiResponse.success("Products fetched", service.searchProduct(productCode)));
	}

	/* ================= NEXT CODE ================= */
//	@GetMapping("/next-code")
//	public ResponseEntity<?> getNextCode() {
//		return ResponseEntity.ok(java.util.Map.of("productCode", service.getNextProductCode()));
//	}
	@GetMapping("/next-code")
	public ResponseEntity<?> getNextCode() {

	    Map<String, Object> map = new java.util.HashMap<>();
	    map.put("productCode", service.getNextProductCode());

	    return ResponseEntity.ok(map);
	}
	
	@GetMapping("/search-lite")
	public ResponseEntity<ApiResponse<List<Map<String, Object>>>> searchLite(@RequestParam String q) {

		return ResponseEntity.ok(ApiResponse.success("Products fetched", service.searchProductLite(q)));
	}

}
