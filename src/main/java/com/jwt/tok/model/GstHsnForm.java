package com.jwt.tok.model;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "gsthsn")
@Data
public class GstHsnForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 AUTO GENERATED CODE
    @Column(name = "CODE", unique = true)
    private String code;

    @Column(name = "DES")
    private String description;

    @Column(name = "SGST")
    private String sgst;

    @Column(name = "CGST")
    private String cgst;

    @Column(name = "IGST")
    private String igst;

    @Column(name = "UGST")
    private String ugst;
}