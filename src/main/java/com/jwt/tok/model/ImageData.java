package com.jwt.tok.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "imagedata")
@Data
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String docno;   // 🔴 mandatory

    @Column(nullable = false)
    private String srno;

    private String loc;

    private String img1; // image path
}