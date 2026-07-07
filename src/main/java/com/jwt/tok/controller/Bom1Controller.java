package com.jwt.tok.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.Bom1;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.Bom1Service;

@RestController
@RequestMapping("/api/bom")
@CrossOrigin("*")
public class Bom1Controller {

    @Autowired
    private Bom1Service service;

    // ✅ CREATE
    @PostMapping("/create")
    public ApiResponse<Bom1> create(@RequestBody Bom1 bom) {
        return ApiResponse.success("BOM Created", service.create(bom));
    }

    // ✅ UPDATE
    @PutMapping("/update/{id}")
    public ApiResponse<Bom1> update(@PathVariable Long id, @RequestBody Bom1 bom) {
        return ApiResponse.success("BOM Updated", service.update(id, bom));
    }

    // ✅ GET ALL
    @GetMapping("/all")
    public ApiResponse<List<Bom1>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ApiResponse<Bom1> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    // ✅ DELETE
    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.success("Deleted", null);
    }

    // 🔍 SEARCH API (JSP replacement)
    @GetMapping("/search")
    public ApiResponse<List<Bom1>> search(@RequestParam String keyword) {
        return ApiResponse.success(service.search(keyword));
    }
}