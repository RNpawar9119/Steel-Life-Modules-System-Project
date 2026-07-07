package com.jwt.tok.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.MemoryMargin;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.MemoryMarginService;

@RestController
@RequestMapping("/api/memory-margin")
@CrossOrigin("*")
public class MemoryMarginController {

    private final MemoryMarginService service;

    public MemoryMarginController(MemoryMarginService service) {
        this.service = service;
    }

    // ================= SAVE =================
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<MemoryMargin>> save(@RequestBody MemoryMargin mm) {

        MemoryMargin saved = service.save(mm);

        return ResponseEntity.ok(
                ApiResponse.success("Memory Margin saved successfully", saved)
        );
    }

    // ================= GET ALL =================
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<MemoryMargin>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.success("Data fetched", service.getAll())
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemoryMargin>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(service.getById(id))
        );
    }

    // ================= UPDATE =================
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<MemoryMargin>> update(
            @PathVariable Long id,
            @RequestBody MemoryMargin mm) {

        MemoryMargin updated = service.update(id, mm);

        return ResponseEntity.ok(
                ApiResponse.success("Updated successfully", updated)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Deleted successfully", null)
        );
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MemoryMargin>>> search(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                ApiResponse.success(service.search(keyword))
        );
    }
}