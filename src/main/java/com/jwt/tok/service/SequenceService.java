package com.jwt.tok.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.tok.model.Sequence;
import com.jwt.tok.repository.SequenceRepository;

@Service
public class SequenceService {

	@Autowired
	private SequenceRepository sequenceRepository;

	public Sequence save(Sequence sequence) {
		if (sequence.getProcesses() != null) {
			sequence.getProcesses().forEach(p -> p.setSequence(sequence));
		}

		return sequenceRepository.save(sequence);
	}

	// GET ALL
	public List<Sequence> getAll() {
		return sequenceRepository.findAll();
	}

	// GET BY ID
	public Sequence getById(Long id) {
		return sequenceRepository.findById(id).orElseThrow(() -> new RuntimeException("Sequence not found"));
	}

	// UPDATE
	public Sequence update(Long id, Sequence updated) {

		Sequence existing = getById(id);

		existing.setSequenceName(updated.getSequenceName());

		existing.getProcesses().clear();

		if (updated.getProcesses() != null) {
			updated.getProcesses().forEach(p -> {
				p.setSequence(existing);
				existing.getProcesses().add(p);
			});
		}

		return sequenceRepository.save(existing);
	}

	// DELETE
	public void delete(Long id) {
		sequenceRepository.deleteById(id);
	}

	// SEARCH
	public List<Sequence> searchByName(String keyword) {

		if (keyword == null || keyword.trim().isEmpty()) {
			return sequenceRepository.findAll();
		}

		return sequenceRepository.findBySequenceNameContainingIgnoreCase(keyword.trim());
	}
}
