package com.jwt.tok.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bom1")
@Data
public class Bom1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   

    @Column(name = "RID")
    private String rid;

    @Column(name = "DES")
    private String des;

    @Column(name = "QTY")
    private String qty;

    @Column(name = "val")
    private String val;

    @Column(name = "cost")
    private String cost;

    @Column(name = "MET")
    private String met;

    @Column(name = "costc")
    private String costc;

    @Column(name = "costd")
    private String costd;

    @Column(name = "w")
    private String w;

    @Column(name = "h")
    private String h;

    @Column(name = "d")
    private String d;

    @Column(name = "q")
    private String q;
}