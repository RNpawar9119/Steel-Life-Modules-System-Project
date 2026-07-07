package com.jwt.tok.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.jwt.tok.model.TaxMaster;
import com.jwt.tok.service.TaxMasterService;

@RestController
@RequestMapping("/api/tax-master")
@CrossOrigin(origins = "*")
public class TaxMasterController {

	@Autowired
	private TaxMasterService taxMasterService;

	@PostMapping("/save-tax")
	public ResponseEntity<TaxMaster> saveTax(@RequestBody TaxMaster taxMaster) {
		return ResponseEntity.ok(taxMasterService.saveTax(taxMaster));
	}

	@GetMapping("/getall-tax")
	public ResponseEntity<List<TaxMaster>> getAllTaxes() {
		return ResponseEntity.ok(taxMasterService.getAllTaxes());
	}

	@GetMapping("/getbyIdTax/{id}")
	public ResponseEntity<TaxMaster> getTaxById(@PathVariable Long id) {
		return ResponseEntity.ok(taxMasterService.getTaxById(id));
	}

	@PutMapping("/update-tax/{id}")
	public ResponseEntity<TaxMaster> updateTax(@PathVariable Long id, @RequestBody TaxMaster taxMaster) {
		return ResponseEntity.ok(taxMasterService.updateTax(id, taxMaster));
	}

	@DeleteMapping("/deletebyId/{id}")
	public ResponseEntity<String> deleteTax(@PathVariable Long id) {
		taxMasterService.deleteTax(id);
		return ResponseEntity.ok("Tax deleted successfully");
	}
}
