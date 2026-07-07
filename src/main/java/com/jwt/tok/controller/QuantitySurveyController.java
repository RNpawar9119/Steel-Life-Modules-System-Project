package com.jwt.tok.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.QuantitySurvey;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.QuantitySurveyService;

@RestController
@RequestMapping("/api/quantity-survey")
@CrossOrigin("*")
public class QuantitySurveyController {

    private final QuantitySurveyService service;

    public QuantitySurveyController(QuantitySurveyService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<QuantitySurvey>> save(@RequestBody QuantitySurvey survey) {

        return ResponseEntity.ok(
                ApiResponse.success("Saved successfully", service.save(survey))
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<QuantitySurvey>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.success(service.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuantitySurvey>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(service.getById(id))
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<QuantitySurvey>> update(
            @PathVariable Long id,
            @RequestBody QuantitySurvey survey) {

        return ResponseEntity.ok(
                ApiResponse.success("Updated successfully", service.update(id, survey))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Deleted successfully", null)
        );
    }
    
    @GetMapping("/next-survey-no")
    public ResponseEntity<ApiResponse<String>> getNextSurveyNo() {

        Long maxSurveyNo = service.getMaxSurveyNo();

        String nextNo = (maxSurveyNo == null) ? "101" : String.valueOf(maxSurveyNo + 1);

        return ResponseEntity.ok(ApiResponse.success(nextNo));
    }
}