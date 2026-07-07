package com.jwt.tok.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.Bom1;
import com.jwt.tok.repository.Bom1Repository;

@Service
public class Bom1Service {

    @Autowired
    private Bom1Repository repository;

    // ✅ CREATE
    public Bom1 create(Bom1 bom) {

        // 🔥 Get max RID
        Long maxRid = repository.getMaxRid();

        Long nextRid;

        if (maxRid == null) {
            nextRid = 101L; // start
        } else {
            nextRid = maxRid + 1;
        }

        // 🔥 Set auto RID
        bom.setRid(String.valueOf(nextRid));

        return repository.save(bom);
    }
    // ✅ UPDATE
    public Bom1 update(Long id, Bom1 bom) {

        Bom1 existing = repository.findById(id)
                .orElseThrow(() -> new ApiException("BOM not found"));

        existing.setRid(bom.getRid());
        existing.setDes(bom.getDes());
        existing.setQty(bom.getQty());
        existing.setVal(bom.getVal());
        existing.setCost(bom.getCost());
        existing.setMet(bom.getMet());
        existing.setCostc(bom.getCostc());
        existing.setCostd(bom.getCostd());
        existing.setW(bom.getW());
        existing.setH(bom.getH());
        existing.setD(bom.getD());
        existing.setQ(bom.getQ());

        return repository.save(existing);
    }

    // ✅ GET ALL
    public List<Bom1> getAll() {
        return repository.findAll();
    }

    // ✅ GET BY ID
    public Bom1 getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException("BOM not found"));
    }

    // ✅ DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // 🔍 SEARCH (same JSP logic)
    public List<Bom1> search(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }

        return repository.searchBom(keyword);
    }
}