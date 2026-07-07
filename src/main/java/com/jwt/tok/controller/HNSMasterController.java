package com.jwt.tok.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.HNSMaster;
import com.jwt.tok.service.HNSMasterService;

@RestController
@RequestMapping("/api/hns-master")
@CrossOrigin(origins = "*")
public class HNSMasterController {

    @Autowired
    private HNSMasterService hnsMasterService;

    
    @PostMapping("/hsn-save")
    public ResponseEntity<HNSMaster> save(@RequestBody HNSMaster hnsMaster) {
        return ResponseEntity.ok(hnsMasterService.save(hnsMaster));
    }

    // ✅ GET ALL
    @GetMapping("/hns-getAll")
    @PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
    public ResponseEntity<List<HNSMaster>> getAll() {
        return ResponseEntity.ok(hnsMasterService.getAll());
    }

    // ✅ GET BY ID
    @GetMapping("/hns-getById/{id}")
    @PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
    public ResponseEntity<HNSMaster> getById(@PathVariable Long id) {
        return ResponseEntity.ok(hnsMasterService.getById(id));
    }

    // ✅ UPDATE
    @PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
    @PutMapping("/hns-update/{id}")
    public ResponseEntity<HNSMaster> update(
            @PathVariable Long id,
            @RequestBody HNSMaster hnsMaster) {
        return ResponseEntity.ok(hnsMasterService.update(id, hnsMaster));
    }

    // ✅ DELETE
    @PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
    @DeleteMapping("/hns-delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        hnsMasterService.delete(id);
        return ResponseEntity.ok("HNS deleted successfully");
    }
}
