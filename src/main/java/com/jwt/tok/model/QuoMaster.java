package com.jwt.tok.model;

import java.util.List;

import javax.persistence.*;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;



@Entity
@Data
public class QuoMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quotationNo;
    private String date;
    private String customerName;
    private String type;
    private String custTypeNo;
    private String address;
    private String email;
    private String mobile1;
    private String mobile2;
    private String salesExecutive;
    private String description;

    // cost
    private String mc;
    private String cc;
    private String ct;
    private String dc;
    private String dt;
    private String disOnBOM;
    private String hardwareDisc;
    private String cd;
    private String dd;

 
    @OneToMany(mappedBy = "quoMaster", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<QuoMasterItem> items;

    
    @OneToMany(mappedBy = "quoMaster", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<QuoMasterItem2> imageItems;

}