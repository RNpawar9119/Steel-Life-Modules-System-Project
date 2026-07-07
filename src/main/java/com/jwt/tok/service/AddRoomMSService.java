package com.jwt.tok.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.AddRoomMS;
import com.jwt.tok.repository.AddRoomRepository;

@Service
public class AddRoomMSService {

    private final AddRoomRepository repo;

    public AddRoomMSService(AddRoomRepository repo) {
        this.repo = repo;
    }

    // ================= SAVE =================
    public AddRoomMS save(AddRoomMS room) {

        if (room.getDescription() == null || room.getDescription().trim().isEmpty()) {
            throw new ApiException("Description is required");
        }

        // 🔥 Step 1: Save first to get ID
        AddRoomMS saved = repo.save(room);

        // 🔥 Step 2: Generate RID = ID + 1
        saved.setRid(saved.getId() + 1);

        // 🔥 Step 3: Save again with RID
        return repo.save(saved);
    }

    // ================= GET ALL =================
    public List<AddRoomMS> getAll() {
        return repo.findAll();
    }

    // ================= GET BY ID =================
    public AddRoomMS getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException("Room not found with id: " + id));
    }

    // ================= UPDATE =================
    public AddRoomMS update(Long id, AddRoomMS updated) {

        AddRoomMS existing = getById(id);

        existing.setDescription(updated.getDescription());

        return repo.save(existing);
    }

    // ================= DELETE =================
    public void delete(Long id) {
        AddRoomMS room = getById(id);
        repo.delete(room);
    }

    // ================= SEARCH =================
    public List<AddRoomMS> search(String keyword) {
        return repo.findByDescriptionContainingIgnoreCase(keyword);
    }
    
    public List<AddRoomMS> searchOrAll(String keyword) {

        // ✅ if empty OR null → return ALL (double click case)
        if (keyword == null || keyword.trim().isEmpty()) {
            return repo.findAll();
        }
        return repo.findByDescriptionContainingIgnoreCase(keyword.trim());
    }
    
    public List<AddRoomMS> autoSearch(String keyword) {

        // ✅ जर काहीच टाइप नाही केलं → FULL DATA
        if (keyword == null || keyword.trim().isEmpty()) {
            return repo.findAll();
        }

        // ✅ Partial search (auto search)
        return repo.findByDescriptionContainingIgnoreCase(keyword.trim());
    }
}