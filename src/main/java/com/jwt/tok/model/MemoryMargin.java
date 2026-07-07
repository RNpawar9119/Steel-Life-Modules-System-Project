//package com.jwt.tok.model;
//
//import javax.persistence.*;
//
//import lombok.Data;
//
//@Entity
//@Data
//public class MemoryMargin {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String type;
//
//    private String cusp;
//
//    @Column(nullable = false)
//    private String description;
//
//    private String delp;
//
//    private String w1;
//    private String w2;
//
//    private String dc;
//    private String dd;
//}



package com.jwt.tok.model;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "marg")   // 🔥 IMPORTANT (DB table connect)
@Data
public class MemoryMargin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TYP")
    private String type;

    @Column(name = "CUSP")
    private String cusp;

    @Column(name = "DELP")
    private String delp;

    @Column(name = "w1")
    private String w1;

    @Column(name = "w2")
    private String w2;

    @Column(name = "DC")
    private String dc;

    @Column(name = "DD")
    private String dd;
}