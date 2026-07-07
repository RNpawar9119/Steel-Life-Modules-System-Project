package com.jwt.tok.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.FormulaHead;
import com.jwt.tok.repository.FormulaHeadRepository;

@Service
public class FormulaHeadService {

    private final FormulaHeadRepository repo;

    public FormulaHeadService(FormulaHeadRepository repo) {
        this.repo = repo;
    }

    // ✅ SAVE (RID AUTO GENERATE)
    public FormulaHead save(FormulaHead formulaHead) {

        if (formulaHead.getDescription() == null || formulaHead.getDescription().trim().isEmpty()) {
            throw new ApiException("Description is required");
        }

        // ❌ payload मधून RID ignore
        formulaHead.setRid(null);

        // 1️⃣ First save → ID generate होईल
        FormulaHead saved = repo.save(formulaHead);

        // 2️⃣ RID = id + 1
        Long nextRid = saved.getId() + 1;

        saved.setRid(String.valueOf(nextRid));

        // 3️⃣ Save again
        return repo.save(saved);
    }

    // ✅ GET ALL
    public List<FormulaHead> getAll() {
        return repo.findAll();
    }

    // ✅ GET BY ID
    public FormulaHead getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException("FormulaHead not found with id: " + id));
    }

      public FormulaHead update(Long id, FormulaHead updated) {

        FormulaHead existing = getById(id);

        if (updated.getDescription() == null || updated.getDescription().trim().isEmpty()) {
            throw new ApiException("Description is required");
        }

        existing.setDescription(updated.getDescription());

        return repo.save(existing);
    }

    // ✅ DELETE
    public void delete(Long id) {

        if (!repo.existsById(id)) {
            throw new ApiException("FormulaHead not found with id: " + id);
        }

        repo.deleteById(id);
    }
    
    public List<FormulaHead> search(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return repo.findAll(); // optional
        }

        return repo.findByDescriptionContainingIgnoreCase(keyword);
    }
}