package com.jwt.tok.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.AddRoomMS;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.AddRoomMSService;

@RestController
@RequestMapping("/api/room-type")
@CrossOrigin("*")
public class AddRoomMSController {

    private final AddRoomMSService service;

    public AddRoomMSController(AddRoomMSService service) {
        this.service = service;
    }

    // ================= SAVE =================
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<AddRoomMS>> save(@RequestBody AddRoomMS room) {

        AddRoomMS saved = service.save(room);

        return ResponseEntity.ok(
                ApiResponse.success("Room saved successfully", saved)
        );
    }

    // ================= GET ALL =================
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AddRoomMS>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.success(service.getAll())
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddRoomMS>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(service.getById(id))
        );
    }

    // ================= UPDATE =================
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<AddRoomMS>> update(
            @PathVariable Long id,
            @RequestBody AddRoomMS room) {

        AddRoomMS updated = service.update(id, room);

        return ResponseEntity.ok(
                ApiResponse.success("Updated successfully", updated)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Deleted successfully", null)
        );
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AddRoomMS>>> search(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                ApiResponse.success(service.search(keyword))
        );
    }
    
    @GetMapping("/searchs")
    public ResponseEntity<ApiResponse<List<AddRoomMS>>> searchOrAll(
            @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok(
                ApiResponse.success(service.searchOrAll(keyword))
        );
    }
    @GetMapping("/auto-search")
    public ResponseEntity<ApiResponse<List<AddRoomMS>>> autoSearch(
            @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok(
                ApiResponse.success(service.autoSearch(keyword))
        );
    }
    
}