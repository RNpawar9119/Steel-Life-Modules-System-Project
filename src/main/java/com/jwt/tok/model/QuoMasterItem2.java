package com.jwt.tok.model;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class QuoMasterItem2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer srNo;
    private String location;

    private String imagePath; 

    @ManyToOne
    @JoinColumn(name = "quo_master_id")
    @JsonBackReference
    private QuoMaster quoMaster;
}