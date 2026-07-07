package com.jwt.tok.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.tok.model.TaxMaster;
import com.jwt.tok.repository.TaxMasterRepository;

@Service
public class TaxMasterService {

	@Autowired
	private TaxMasterRepository taxMasterRepository;

	public TaxMaster saveTax(TaxMaster taxMaster) {
		return taxMasterRepository.save(taxMaster);
	}

	public List<TaxMaster> getAllTaxes() {
		return taxMasterRepository.findAll();
	}

	public TaxMaster getTaxById(Long id) {
		return taxMasterRepository.findById(id).orElseThrow(() -> new RuntimeException("Tax not found with id " + id));
	}

	public TaxMaster updateTax(Long id, TaxMaster taxMaster) {
		TaxMaster existingTax = getTaxById(id);
		existingTax.setTaxPercentage(taxMaster.getTaxPercentage());
		return taxMasterRepository.save(existingTax);
	}

	public void deleteTax(Long id) {
		taxMasterRepository.deleteById(id);
	}
}
