package com.jwt.tok.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tok.model.QuoMaster;
import com.jwt.tok.service.QuoMasterService;

@RestController
@RequestMapping("/api/quo-master")
@CrossOrigin("*")
public class QuoMasterController {

    private final QuoMasterService service;

    public QuoMasterController(QuoMasterService service) {
        this.service = service;
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addQuotation(@RequestBody QuoMaster quo) {
        return ResponseEntity.ok(service.addQuotation(quo));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody QuoMaster quo) {
        return ResponseEntity.ok(service.save(quo));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody QuoMaster quo) {
        return ResponseEntity.ok(service.update(id, quo));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}