package com.jwt.tok.model;


import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import lombok.Data;
@Entity
@Data
public class QuoMasterItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer srNo;
    private String location;
    private String description;
    private String size;

    private String materialCost;
    private String discCustomer;
    private String discDealer;

    private String customerCash;
    private String customerTax;
    private String dealerCash;
    private String dealerTax;

    @ManyToOne
    @JoinColumn(name = "quo_master_id")
    @JsonBackReference
    private QuoMaster quoMaster;
}
