package com.jwt.tok.service;

import com.jwt.tok.dao.SlqDao;
import com.jwt.tok.dto.ImageUploadDTO;
import com.jwt.tok.model.ImageData;
import com.jwt.tok.repository.ImageDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class ImageDataService {

    @Autowired
    private ImageDataRepository repo;
    
    @Autowired
    private SlqDao slqDao;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadImage(ImageUploadDTO dto) throws Exception {

        if (dto.getDocno() == null || dto.getDocno().isEmpty()) {
            throw new RuntimeException("docno is mandatory");
        }

        // 🔥 NEW VALIDATION (MAIN FIX)
        if (!slqDao.isDocNoExist(dto.getDocno())) {
            throw new RuntimeException("Invalid docno: Not present in SLQ");
        }

        if (dto.getFile() == null || dto.getFile().isEmpty()) {
            throw new RuntimeException("file is mandatory");
        }

        // 🔥 AUTO SRNO GENERATION
        Integer maxSrno = repo.findMaxSrnoByDocno(dto.getDocno());
        int newSrno = (maxSrno == null ? 1 : maxSrno + 1);

        // 👉 FILE TYPE LOGIC
        String contentType = dto.getFile().getContentType();
        String folderPath;

        if (contentType != null && contentType.startsWith("image")) {
            folderPath = uploadDir + "imagedata/images/";
        } else {
            folderPath = uploadDir + "imagedata/files/";
        }

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + dto.getFile().getOriginalFilename();
        String fullPath = folderPath + fileName;

        dto.getFile().transferTo(new File(fullPath));

        String dbPath = (contentType != null && contentType.startsWith("image"))
                ? "uploads/imagedata/images/" + fileName
                : "uploads/imagedata/files/" + fileName;

        ImageData img = new ImageData();
        img.setDocno(dto.getDocno());
        img.setSrno(String.valueOf(newSrno)); // 🔥 AUTO
        img.setLoc(dto.getLoc());
        img.setImg1(dbPath);

        repo.save(img);

        return "Uploaded successfully with srno = " + newSrno;
    }

    // ✅ Get by docno
    public List<ImageData> getByDocno(String docno) {
        return repo.findByDocno(docno);
    }

    // ✅ Delete
    public String delete(String docno, String srno) {
        repo.deleteByDocnoAndSrno(docno, srno);
        return "Deleted successfully";
    }
    
    public String updateImage(ImageUploadDTO dto) throws Exception {

        if (dto.getDocno() == null || dto.getDocno().isEmpty()) {
            throw new RuntimeException("docno is mandatory");
        }

        // 🔥 ADD THIS ALSO
        if (!slqDao.isDocNoExist(dto.getDocno())) {
            throw new RuntimeException("Invalid docno: Not present in SLQ");
        }

        if (dto.getSrno() == null || dto.getSrno().isEmpty()) {
            throw new RuntimeException("srno is mandatory for update");
        }

        ImageData existing = repo.findByDocnoAndSrno(dto.getDocno(), dto.getSrno())
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // 👉 file update (optional)
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {

            String contentType = dto.getFile().getContentType();
            String folderPath;

            if (contentType != null && contentType.startsWith("image")) {
                folderPath = uploadDir + "imagedata/images/";
            } else {
                folderPath = uploadDir + "imagedata/files/";
            }

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + dto.getFile().getOriginalFilename();
            String fullPath = folderPath + fileName;

            dto.getFile().transferTo(new File(fullPath));

            String dbPath = (contentType != null && contentType.startsWith("image"))
                    ? "uploads/imagedata/images/" + fileName
                    : "uploads/imagedata/files/" + fileName;

            existing.setImg1(dbPath);
        }

        // 👉 loc update
        if (dto.getLoc() != null) {
            existing.setLoc(dto.getLoc());
        }

        repo.save(existing);

        return "Updated successfully";
    }
}