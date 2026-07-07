package com.jwt.tok.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.ItemDiscription;
import com.jwt.tok.repository.ItemDiscriptionRepository;

@Service
public class ItemDiscriptionService {

	private final ItemDiscriptionRepository repo;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public ItemDiscriptionService(ItemDiscriptionRepository repo) {
		this.repo = repo;
	}

//	public ItemDiscription save(String description, MultipartFile file) throws Exception {
//
//		if (description == null || description.trim().isEmpty()) {
//			throw new ApiException("Description is required");
//		}
//
//		ItemDiscription item = new ItemDiscription();
//		item.setDescription(description);
//
//		if (file != null && !file.isEmpty()) {
//			item.setPic(saveFile(file));
//		}
//
//		return repo.save(item);
//	}
	
	public ItemDiscription save(String description, MultipartFile file) throws Exception {

	    if (description == null || description.trim().isEmpty()) {
	        throw new ApiException("Description is required");
	    }

	    ItemDiscription item = new ItemDiscription();
	    item.setDescription(description);

	    // 🔥 image save
	    if (file != null && !file.isEmpty()) {
	        item.setPic(saveFile(file));
	    }

	    // 🔥 Step 1: save → id मिळेल
	    ItemDiscription saved = repo.save(item);

	    // 🔥 Step 2: generate RID
	    Long nextRid = saved.getId() + 1;
	    saved.setRid(String.valueOf(nextRid));

	    // 🔥 Step 3: save again
	    return repo.save(saved);
	}

	public List<ItemDiscription> getAll() {
		return repo.findAll();
	}

	public ItemDiscription getById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ApiException("Item not found"));
	}

//	public ItemDiscription update(Long id, String description, MultipartFile file) throws Exception {
//
//		ItemDiscription item = getById(id);
//
//		item.setDescription(description);
//
//		if (file != null && !file.isEmpty()) {
//			item.setPic(saveFile(file));
//		}
//
//		return repo.save(item);
//	}
	
	public ItemDiscription update(Long id, String description, MultipartFile file) throws Exception {

	    ItemDiscription item = getById(id);

	    if (description == null || description.trim().isEmpty()) {
	        throw new ApiException("Description is required");
	    }

	    item.setDescription(description);

	    // 🔥 new image असेल तर update
	    if (file != null && !file.isEmpty()) {
	        item.setPic(saveFile(file));
	    }

	    // ❌ RID change करायचा नाही
	    return repo.save(item);
	}

	public void delete(Long id) {
		ItemDiscription item = getById(id);
		repo.delete(item);
	}

	private String saveFile(MultipartFile file) throws Exception {

		String dirPath = uploadDir + "/item-description";
		File dir = new File(dirPath);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

		Path path = Paths.get(dirPath, fileName);

		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		return "/uploads/item-description/" + fileName;
	}
	
	// 🔥 AUTO SEARCH
	public List<ItemDiscription> search(String keyword) {

	    if (keyword == null || keyword.trim().isEmpty()) {
	        return repo.findAll(); // no keyword = all data
	    }

	    return repo.findByDescriptionContainingIgnoreCase(keyword);
	}
}