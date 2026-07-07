package com.jwt.tok.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.GstHsnForm;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.GstHsnFormService;

@RestController
@RequestMapping("/api/gst-hsn")
@CrossOrigin("*")
public class GstHsnFormController {

    private final GstHsnFormService service;

    public GstHsnFormController(GstHsnFormService service) {
        this.service = service;
    }

    // ================= SAVE =================
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<GstHsnForm>> save(@RequestBody GstHsnForm gst) {

        GstHsnForm saved = service.save(gst);

        return ResponseEntity.ok(
                ApiResponse.success("Saved successfully", saved)
        );
    }

    // ================= GET ALL =================
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<GstHsnForm>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.success(service.getAll())
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GstHsnForm>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(service.getById(id))
        );
    }

    // ================= UPDATE =================
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<GstHsnForm>> update(
            @PathVariable Long id,
            @RequestBody GstHsnForm gst) {

        GstHsnForm updated = service.update(id, gst);

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
}