package com.jwt.tok.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // 🔥 Primary Key (API operations)

    @Column(name = "enquiry_id", unique = true, nullable = false, updatable = false)
    private String enquiryId;   // 🔥 Business ID (ENQ/2026/0001)

    private String customerName;
    private String partyName;
    private String address;
    private String city;
    private String contactNo;
    private String email;
    private String product;
    private String referredBy;

    private String dealerCode;

    @Transient
    private String dealerName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date = LocalDate.now();

    private String resource;
    private String status;

    private String enquiryForm;
    private String finalizationReport;
    private String actualDimension;
    private String design2D3D;

    @Column(name = "quotation_created")
    private Boolean quotationCreated = false;

}