package com.jwt.tok.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.FormulaHead;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.FormulaHeadService;

@RestController
@RequestMapping("/api/formula-head")
@CrossOrigin("*")
public class FormulaHeadController {

    private final FormulaHeadService service;

    public FormulaHeadController(FormulaHeadService service) {
        this.service = service;
    }

    // ✅ SAVE
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
    public ResponseEntity<ApiResponse<FormulaHead>> save(@RequestBody FormulaHead formulaHead) {

        FormulaHead saved = service.save(formulaHead);

        return ResponseEntity.ok(
                ApiResponse.success("Formula Head saved successfully", saved)
        );
    }

    // ✅ GET ALL
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
    public ResponseEntity<ApiResponse<List<FormulaHead>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.success(service.getAll())
        );
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
    public ResponseEntity<ApiResponse<FormulaHead>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(service.getById(id))
        );
    }

    // ✅ UPDATE
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
    public ResponseEntity<ApiResponse<FormulaHead>> update(
            @PathVariable Long id,
            @RequestBody FormulaHead formulaHead) {

        FormulaHead updated = service.update(id, formulaHead);

        return ResponseEntity.ok(
                ApiResponse.success("Formula Head updated successfully", updated)
        );
    }

    // ✅ DELETE
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('Admin')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Formula Head deleted successfully", null)
        );
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FormulaHead>>> search(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                ApiResponse.success(service.search(keyword))
        );
    }
}