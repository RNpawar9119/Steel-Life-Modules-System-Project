package com.jwt.tok.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.SideType;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.SideTypeService;

@RestController
@RequestMapping("/api/side-types")
@CrossOrigin
public class SideTypeController {

    @Autowired
    private SideTypeService service;

    @PostMapping("/create")
    public ApiResponse<SideType> create(@RequestBody SideType sideType) {
        return ApiResponse.success("Side Type Created Successfully", service.save(sideType));
    }

    @GetMapping("/all")
    public ApiResponse<List<SideType>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<SideType> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<SideType> update(@PathVariable Long id, @RequestBody SideType sideType) {
        return ApiResponse.success("Side Type Updated Successfully", service.update(id, sideType));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.success("Side Type Deleted Successfully", null);
    }
}