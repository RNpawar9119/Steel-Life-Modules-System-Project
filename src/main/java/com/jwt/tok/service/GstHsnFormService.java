package com.jwt.tok.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.GstHsnForm;
import com.jwt.tok.repository.GstHsnFormRepository;

@Service
public class GstHsnFormService {

    private final GstHsnFormRepository repo;

    public GstHsnFormService(GstHsnFormRepository repo) {
        this.repo = repo;
    }

    // ================= SAVE =================
    public GstHsnForm save(GstHsnForm gst) {

        if (gst.getDescription() == null || gst.getDescription().trim().isEmpty()) {
            throw new ApiException("Description is required");
        }

        // 🔥 AUTO GENERATE CODE
        GstHsnForm last = repo.findTopByOrderByIdDesc();

        String newCode;

        if (last == null || last.getCode() == null) {
            newCode = "1001";  // starting code
        } else {
            int next = Integer.parseInt(last.getCode()) + 1;
            newCode = String.valueOf(next);
        }

        gst.setCode(newCode);

        return repo.save(gst);
    }

    // ================= GET ALL =================
    public List<GstHsnForm> getAll() {
        return repo.findAll();
    }

    // ================= GET BY ID =================
    public GstHsnForm getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException("Record not found with id: " + id));
    }

    // ================= UPDATE =================
    public GstHsnForm update(Long id, GstHsnForm updated) {

        GstHsnForm existing = getById(id);

        existing.setDescription(updated.getDescription());
        existing.setSgst(updated.getSgst());
        existing.setCgst(updated.getCgst());
        existing.setIgst(updated.getIgst());
        existing.setUgst(updated.getUgst());

        // 🔥 CODE change नाही करायचा
        return repo.save(existing);
    }

    // ================= DELETE =================
    public void delete(Long id) {

        if (!repo.existsById(id)) {
            throw new ApiException("Record not found");
        }

        repo.deleteById(id);
    }
}