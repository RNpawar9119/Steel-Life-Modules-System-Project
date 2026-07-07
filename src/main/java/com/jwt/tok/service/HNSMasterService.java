package com.jwt.tok.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.tok.model.HNSMaster;
import com.jwt.tok.repository.HNSMasterRepository;

@Service
public class HNSMasterService {

    @Autowired
    private HNSMasterRepository hnsMasterRepository;

    // ✅ SAVE
    public HNSMaster save(HNSMaster hnsMaster) {
        return hnsMasterRepository.save(hnsMaster);
    }

    // ✅ GET ALL
    public List<HNSMaster> getAll() {
        return hnsMasterRepository.findAll();
    }

    // ✅ GET BY ID
    public HNSMaster getById(Long id) {
        return hnsMasterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HNS not found with id " + id));
    }

    // ✅ UPDATE
    public HNSMaster update(Long id, HNSMaster hnsMaster) {
        HNSMaster existing = getById(id);
        existing.setHnsCode(hnsMaster.getHnsCode());
        existing.setTax(hnsMaster.getTax());
        return hnsMasterRepository.save(existing);
    }

    // ✅ DELETE
    public void delete(Long id) {
        hnsMasterRepository.deleteById(id);
    }
}
