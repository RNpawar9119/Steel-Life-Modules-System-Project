package com.jwt.tok.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tok.model.Slq;
import com.jwt.tok.model.Slq1;
import com.jwt.tok.model.Slq2;
import com.jwt.tok.service.SlqService;

@RestController
@RequestMapping("/api/slq")
@CrossOrigin("*")
public class SlqController {

	@Autowired
	private SlqService service;

	@PostMapping("/header")
	public Slq saveHeader(@RequestBody Slq slq) {
		return service.saveSlq(slq);
	}
	
	//doc no displlay on frentend quotation number genarate
	@GetMapping("/next-docno")
	public String getNextDocNo() {
	    return service.getNextDocNo("2026", "SLQ");
	}

	@PostMapping("/item")
	public Slq1 addItem(@RequestBody Slq1 item) {
		return service.saveItem(item);
	}

	@GetMapping("/items")
	public List<Map<String, Object>> getItems(@RequestParam String docno) {
		return service.getItems(docno);
	}

	@GetMapping("/details")
	public List<Slq2> details(@RequestParam String docno, @RequestParam String itemno) {

		return service.getItemDetails("2025", "SLQ", docno, itemno);
	}

	// process dom
	@PostMapping("/process")
	public String process(@RequestParam String docno, @RequestParam String itemno) {

		service.processBom("2025", "SLQ", docno, itemno, "1");
		return "Processed";
	}

	// totals
	@GetMapping("/totals")
	public Map<String, Object> totals(@RequestParam String docno, @RequestParam String itemno) {

		return service.getTotals("2025", "SLQ", docno, itemno);
	}

	// delete
	@DeleteMapping("/delete")
	public String delete(@RequestParam String docno) {
		service.deleteQuotation("2025", "SLQ", docno);
		return "Deleted";
	}

//	@PostMapping("/slq2/save")
//	public List<Slq2> saveSlq2(@RequestParam String docno, @RequestParam String itemno, @RequestBody List<Slq2> list) {
//
//		return service.saveAndCalculateSlq2("2025", "SLQ", docno, itemno, list);
//	}

	@PostMapping("/slq2/save")
	public Map<String, Object> saveSlq2(@RequestParam String docno, @RequestParam String itemno,
			@RequestBody List<Slq2> list) {

		return service.saveAndCalculateSlq2("2026", "SLQ", docno, itemno, list);
	}

	@GetMapping("/all")
	public List<Map<String, Object>> getAll(@RequestParam(required = false) String fyear) {

		return service.getAllQuotations(fyear);
	}

	@PostMapping("/final")
	public Map<String, Object> finalAmount(@RequestParam String docno, @RequestParam String type) {
	    return service.calculateFinal("2026", "SLQ", docno, type);
	}

	@GetMapping("/btyp")
	public List<String> getBomTypes() {
		return service.getBomTypes();
	}

	@GetMapping("/custtype")
	public List<String> getCustomerTypes() {
		return service.getCustomerTypes();
	}

	@PostMapping("/update")
	public Map<String, Object> updateQuotation(@RequestParam String docno, @RequestBody Map<String, Object> payload) {

		return service.updateQuotation("2026", "SLQ", docno, payload);
	}
	@GetMapping("/get")
	public Map<String, Object> getQuotation(@RequestParam String docno) {
	    return service.getQuotation("2026", "SLQ", docno);
	}

}