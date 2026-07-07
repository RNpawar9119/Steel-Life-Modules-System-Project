package com.jwt.tok.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jwt.tok.model.Sequence;
import com.jwt.tok.service.SequenceService;

@RestController
@RequestMapping("/api/sequence")
@CrossOrigin(origins = "*")
public class SequenceController {

    @Autowired
    private SequenceService sequenceService;

    @PostMapping("/save")
    public ResponseEntity<Sequence> save(@RequestBody Sequence sequence) {
        return ResponseEntity.ok(sequenceService.save(sequence));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Sequence>> getAll() {
        return ResponseEntity.ok(sequenceService.getAll());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Sequence> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sequenceService.getById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Sequence> update(
            @PathVariable Long id,
            @RequestBody Sequence sequence) {
        return ResponseEntity.ok(sequenceService.update(id, sequence));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        sequenceService.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
    @GetMapping("/search")
    public ResponseEntity<List<Sequence>> search(
            @RequestParam("name") String name) {

        return ResponseEntity.ok(
                sequenceService.searchByName(name)
        );
    }

}
