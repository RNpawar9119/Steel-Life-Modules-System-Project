//package com.jwt.tok.controller;
//
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jwt.tok.dto.QuotationRequest;
//import com.jwt.tok.model.QuotationMaster;
//import com.jwt.tok.response.ApiResponse;
//import com.jwt.tok.service.QuotationService;
//import com.jwt.tok.util.JwtUtil;
//
//@RestController
//@RequestMapping("/api/quotation")
//@CrossOrigin
//public class QuotationController {
//
//    @Autowired
//    private QuotationService quotationService;
//
//    @PostMapping("/save")
//    public ResponseEntity<?> saveQuotation(@RequestBody QuotationRequest request) {
//
//        String response = quotationService.saveQuotation(request);
//
//        return ResponseEntity.ok(response);
//    }
//}