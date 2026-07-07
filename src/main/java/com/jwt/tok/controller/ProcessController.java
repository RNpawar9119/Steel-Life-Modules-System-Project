
package com.jwt.tok.controller;

import com.jwt.tok.model.Process;
import com.jwt.tok.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    @Autowired
    private ProcessRepository processRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
    public ResponseEntity<List<Process>> getAllProcesses() {
        List<Process> processes = processRepository.findAll();
        return new ResponseEntity<>(processes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Process> createProcess(@RequestBody Process process) {
        Process savedProcess = processRepository.save(process);
        return new ResponseEntity<>(savedProcess, HttpStatus.CREATED);
    }


@PutMapping("/{id}")
public ResponseEntity<Process> updateProcess(@PathVariable Long id, @RequestBody Process processDetails) {
    return processRepository.findById(id)
            .map(process -> {
                process.setName(processDetails.getName());
                // Set other fields as needed
                Process updatedProcess = processRepository.save(process);
                return new ResponseEntity<>(updatedProcess, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Long id) {
        if (!processRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        processRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
