package com.jwt.tok.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.MemoryMargin;
import com.jwt.tok.repository.MemoryMarginRepository;

//@Service
//public class MemoryMarginService {
//
//    private final MemoryMarginRepository repo;
//
//    public MemoryMarginService(MemoryMarginRepository repo) {
//        this.repo = repo;
//    }
//
//    public MemoryMargin save(MemoryMargin mm) {
//
//        if (mm.getType() == null || mm.getType().trim().isEmpty()) {
//            throw new ApiException("Type is required");
//        }
//
//        if (mm.getDescription() == null || mm.getDescription().trim().isEmpty()) {
//            throw new ApiException("Description is required");
//        }
//
//        return repo.save(mm);
//    }
//
//    public List<MemoryMargin> getAll() {
//        return repo.findAll();
//    }
//
//    // ================= GET BY ID =================
//    public MemoryMargin getById(Long id) {
//        return repo.findById(id)
//                .orElseThrow(() -> new ApiException("Memory Margin not found with id: " + id));
//    }
//
//    // ================= UPDATE =================
//    public MemoryMargin update(Long id, MemoryMargin updated) {
//
//        MemoryMargin existing = getById(id);
//
//        existing.setType(updated.getType());
//        existing.setCusp(updated.getCusp());
//        existing.setDescription(updated.getDescription());
//        existing.setDelp(updated.getDelp());
//        existing.setW1(updated.getW1());
//        existing.setW2(updated.getW2());
//        existing.setDc(updated.getDc());
//        existing.setDd(updated.getDd());
//
//        return repo.save(existing);
//    }
//
//    public void delete(Long id) {
//        MemoryMargin mm = getById(id);
//        repo.delete(mm);
//    }
//
//    public List<MemoryMargin> search(String keyword) {
//        return repo.findByTypeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
//                keyword, keyword
//        );
//    }
//}

@Service
public class MemoryMarginService {

    private final MemoryMarginRepository repo;

    public MemoryMarginService(MemoryMarginRepository repo) {
        this.repo = repo;
    }

    // ✅ SAVE
    public MemoryMargin save(MemoryMargin mm) {

        if (mm.getType() == null || mm.getType().trim().isEmpty()) {
            throw new ApiException("Type is required");
        }

        return repo.save(mm);
    }

    // ✅ GET ALL
    public List<MemoryMargin> getAll() {
        return repo.findAll();
    }

    // ✅ GET BY ID
    public MemoryMargin getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException("Memory Margin not found with id: " + id));
    }

    // ✅ UPDATE
    public MemoryMargin update(Long id, MemoryMargin updated) {

        MemoryMargin existing = getById(id);

        existing.setType(updated.getType());
        existing.setCusp(updated.getCusp());
        existing.setDelp(updated.getDelp());
        existing.setW1(updated.getW1());
        existing.setW2(updated.getW2());
        existing.setDc(updated.getDc());
        existing.setDd(updated.getDd());

        return repo.save(existing);
    }

    // ✅ DELETE
    public void delete(Long id) {
        MemoryMargin mm = getById(id);
        repo.delete(mm);
    }

    // ✅ SEARCH (type वरच search)
    public List<MemoryMargin> search(String keyword) {
        return repo.findByTypeContainingIgnoreCase(keyword);
    }
}
