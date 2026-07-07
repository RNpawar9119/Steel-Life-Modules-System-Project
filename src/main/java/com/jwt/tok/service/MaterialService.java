package com.jwt.tok.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.HNSMaster;
import com.jwt.tok.model.Material;
import com.jwt.tok.repository.HNSMasterRepository;
import com.jwt.tok.repository.MaterialRepository;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;
    
    @Autowired
    private HNSMasterRepository hnsRepository;
    
    // ✅ CREATE
//    public Material create(Material material) {
//
//        material.setCode(null);
//
//        Material saved = repository.save(material);
//
//        Long nextCode = saved.getId() + 1;
//        saved.setCode(String.valueOf(nextCode));
//
//        return repository.save(saved);
//    }
    
    public Material create(Material material) {

        // 🔥 Convert HSN ID → Long
        Long hsnId = Long.parseLong(material.getHsn());

        // 🔥 Fetch from HNSMaster
        HNSMaster hns = hnsRepository.findById(hsnId)
            .orElseThrow(() -> new RuntimeException("Invalid HSN ID"));

        // 🔥 Set HSN + TAX
        material.setHsn(hns.getHnsCode());
        material.setTax(String.valueOf(hns.getTax().getTaxPercentage()));

        // 🔥 CODE AUTO
        material.setCode(null);
        Material saved = repository.save(material);

        saved.setCode(String.valueOf(saved.getId()));
        
        return repository.save(saved);
    }
    

    // ✅ UPDATE
//    public Material update(Long id, Material material) {
//
//        Material existing = repository.findById(id)
//                .orElseThrow(() -> new ApiException("Material not found"));
//
//        existing.setTyp(material.getTyp());
//        existing.setGrp(material.getGrp());
//        existing.setDes(material.getDes());
//        existing.setUom(material.getUom());
//        existing.setHsn(material.getHsn());
//        existing.setTax(material.getTax());
//        existing.setRate(material.getRate());
//
//        
//        return repository.save(existing);
//    }
    
    public Material update(Long id, Material material) {

        Material existing = repository.findById(id)
                .orElseThrow(() -> new ApiException("Material not found"));

        existing.setTyp(material.getTyp());
        existing.setGrp(material.getGrp());
        existing.setDes(material.getDes());
        existing.setUom(material.getUom());
        existing.setRate(material.getRate());

        // 🔥 HSN → TAX logic (ID based)
        if (material.getHsn() != null && !material.getHsn().isEmpty()) {

            try {
                Long hsnId = Long.parseLong(material.getHsn());

                HNSMaster hns = hnsRepository.findById(hsnId)
                        .orElseThrow(() -> new RuntimeException("Invalid HSN ID"));

                // ✅ Set actual values
                existing.setHsn(hns.getHnsCode());
                existing.setTax(String.valueOf(hns.getTax().getTaxPercentage()));

            } catch (NumberFormatException e) {
                throw new RuntimeException("HSN must be a valid numeric ID");
            }
        }

        return repository.save(existing);
    }

    public List<Material> getAll() {
        return repository.findAll();
    }

    public Material getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException("Material not found"));
    }

    public void delete(Long id) {
        Material material = getById(id);
        repository.delete(material);
    }
    
    public List<Material> searchByCode(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }

        return repository.searchByCode(keyword);
    }
    public List<Material> getHardware(String keyword) {
        return repository.getHardware(keyword);
    }
    
}