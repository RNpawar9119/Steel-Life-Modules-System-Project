package com.jwt.tok.model;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
@Table(name = "quotation_master")
public class QuotationMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String contactNo;
    private String email;
    private String address;
    private String partyName;
    private String city;
    private String product;
    private LocalDate quotationDate;

    private Double totalAmount;
    private Double totalTax;
    private Double grandTotal;
    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<QuotationItem> items;

    @Column(name = "enquiry_id")
    private Long enquiryId;
   
    private String dealerCode;
}
