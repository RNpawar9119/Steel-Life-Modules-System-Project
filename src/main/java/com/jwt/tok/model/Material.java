package com.jwt.tok.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mate")
@Data
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "typ")
    private String typ;

    @Column(name = "grp")
    private String grp;

    @Column(name = "des")
    private String des;

    @Column(name = "uom")
    private String uom;

    @Column(name = "rate")
    private String rate;   

    @Column(name = "hsn")
    private String hsn;

    @Column(name = "tax")
    private String tax;    
}