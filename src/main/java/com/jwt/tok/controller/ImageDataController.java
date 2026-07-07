package com.jwt.tok.controller;

import com.jwt.tok.dto.ImageUploadDTO;
import com.jwt.tok.model.ImageData;
import com.jwt.tok.service.ImageDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageDataController {

    @Autowired
    private ImageDataService service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(
            @RequestParam("docno") String docno,
            @RequestParam(value = "loc", required = false) String loc,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        ImageUploadDTO dto = new ImageUploadDTO();
        dto.setDocno(docno);
        dto.setLoc(loc);
        dto.setFile(file);

        return service.uploadImage(dto);
    }
    // ✅ Get Images
    @GetMapping("/get/{docno}")
    public List<ImageData> get(@PathVariable String docno) {
        return service.getByDocno(docno);
    }

    // ✅ Delete
    @DeleteMapping("/delete")
    public String delete(@RequestParam String docno,
                         @RequestParam String srno) {
        return service.delete(docno, srno);
    }
    
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String update(
            @RequestParam("docno") String docno,
            @RequestParam("srno") String srno,
            @RequestParam(value = "loc", required = false) String loc,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {

        ImageUploadDTO dto = new ImageUploadDTO();
        dto.setDocno(docno);
        dto.setSrno(srno);
        dto.setLoc(loc);
        dto.setFile(file);

        return service.updateImage(dto);
    }
}