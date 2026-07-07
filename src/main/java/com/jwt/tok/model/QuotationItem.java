package com.jwt.tok.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name = "quotation_item")
public class QuotationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name = "room_id")
    private Long roomId;
    private String room; 
    
    @Column(name = "product_id")
    private Long productId; 
    private String product;

    private Integer quantity;
    private Double unitPrice;

    private Double totalPrice;
    private Double discount;
    private Double totalAfterDisc;

    private String hns;
    private Double taxPercent;
    private Double taxAmount;
    private Double grandTotal;

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    @JsonBackReference
    private QuotationMaster quotation;
}
