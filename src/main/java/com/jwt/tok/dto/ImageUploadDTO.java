package com.jwt.tok.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadDTO {

    private String docno;
    private String srno;
    private String loc;

    private MultipartFile file;   // generic (image/file both)
}