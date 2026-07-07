package com.jwt.tok.model;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "hns_master")
@Data
public class HNSMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hns_code", nullable = false, unique = true)
    private String hnsCode;

    // 🔥 MANY HNS → ONE TAX
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tax_id", nullable = false)
    private TaxMaster tax;
}
