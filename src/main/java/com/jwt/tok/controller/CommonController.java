package com.jwt.tok.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.service.CommonSearchService;

@RestController
@RequestMapping("/api/common")
@CrossOrigin("*")
public class CommonController {

    @Autowired
    private CommonSearchService service;

    @GetMapping("/search-combined") 
    public List<String> searchCombined(
            @RequestParam String type,
            @RequestParam String query) {

        return service.searchCombined(type, query);
    }
}