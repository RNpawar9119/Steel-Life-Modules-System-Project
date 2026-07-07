package com.jwt.tok.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.Material;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.MaterialService;

@RestController
@RequestMapping("/api/materials")
@CrossOrigin("*")
public class MaterialController {

    @Autowired
    private MaterialService service;

    // ✅ CREATE
    @PostMapping("/create")
    public ApiResponse<Material> create(@RequestBody Material material) {
        return ApiResponse.success("Material Created", service.create(material));
    }

    // ✅ UPDATE
    @PutMapping("/update/{id}")
    public ApiResponse<Material> update(
            @PathVariable Long id,
            @RequestBody Material material) {

        return ApiResponse.success("Material Updated", service.update(id, material));
    }

    // ✅ GET ALL
    @GetMapping("/all")
    public ApiResponse<List<Material>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ApiResponse<Material> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    // ✅ DELETE
    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.success("Material Deleted", null);
    }
    @GetMapping("/search-by-code")
    public ApiResponse<List<Material>> searchByCode(@RequestParam String keyword) {
        return ApiResponse.success(service.searchByCode(keyword));
    }
    
    @GetMapping("/hardware")
    public ApiResponse<List<Material>> getHardware(@RequestParam String keyword) {
        return ApiResponse.success(service.getHardware(keyword));
    }
    
}