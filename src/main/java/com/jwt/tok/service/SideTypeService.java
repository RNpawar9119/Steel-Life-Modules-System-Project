//package com.jwt.tok.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.jwt.tok.exception.ApiException;
//import com.jwt.tok.model.SideType;
//import com.jwt.tok.repository.SideTypeRepository;
//
//@Service
//public class SideTypeService {
//
//	@Autowired
//	private SideTypeRepository repository;
//
//	public SideType save(SideType sideType) {
//
//		if (sideType.getDescription() == null || sideType.getDescription().trim().isEmpty()) {
//			throw new ApiException("Description is required");
//		}
//
//		return repository.save(sideType);
//	}
//
//	public List<SideType> getAll() {
//		return repository.findAll();
//	}
//
//	public SideType getById(Long id) {
//		return repository.findById(id).orElseThrow(() -> new ApiException("Side Type not found with id: " + id));
//	}
//
//	public SideType update(Long id, SideType sideType) {
//
//		SideType existing = getById(id);
//
//		existing.setDescription(sideType.getDescription());
//
//		return repository.save(existing);
//	}
//
//	public void delete(Long id) {
//		SideType existing = getById(id);
//		repository.delete(existing);
//	}
//}


package com.jwt.tok.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.SideType;
import com.jwt.tok.repository.SideTypeRepository;

@Service
public class SideTypeService {

    @Autowired
    private SideTypeRepository repository;

    // ✅ CREATE
    public SideType save(SideType sideType) {

        if (sideType.getDescription() == null || sideType.getDescription().trim().isEmpty()) {
            throw new ApiException("Description is required");
        }

        // 🔥 Step 1: Save first to get ID
        SideType saved = repository.save(sideType);

        // 🔥 Step 2: Generate RID = id + 1
        Long nextRid = saved.getId() + 1;

        saved.setRid(String.valueOf(nextRid));

        // 🔥 Step 3: Save again
        return repository.save(saved);
    }

    // ✅ GET ALL
    public List<SideType> getAll() {
        return repository.findAll();
    }

    // ✅ GET BY ID
    public SideType getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException("Side Type not found with id: " + id));
    }

    // ✅ UPDATE
    public SideType update(Long id, SideType sideType) {

        SideType existing = getById(id);

        if (sideType.getDescription() == null || sideType.getDescription().trim().isEmpty()) {
            throw new ApiException("Description is required");
        }

        existing.setDescription(sideType.getDescription());

        // ❌ RID change karaycha nahi
        return repository.save(existing);
    }

    // ✅ DELETE
    public void delete(Long id) {
        SideType existing = getById(id);
        repository.delete(existing);
    }
}