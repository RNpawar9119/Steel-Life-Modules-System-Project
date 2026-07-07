package com.jwt.tok.model;

import lombok.Data;

@Data
public class Slq1 {
  
    // 🔹 Primary Mapping Fields
    private String fyear;
    private String doctype;
    private String docno;
    private String docdt;   // optional

    private String itemno;

    // 🔹 Item Details
    private String loc;     // location
    private String name1;   // description
    private String wdhq;    // width/height/qty

    // 🔹 Amount Fields
    private String amt;     // optional (not always used)
    private String amt1;    // material value

    private String amtcc;   // customer cash
    private String amtct;   // customer tax
    private String amtdc;   // dealer cash
    private String amtdt;   // dealer tax

    // 🔹 Discounts
    private String dca;     // discount customer
    private String dda;     // discount dealer

    // 🔹 Images
    private String img1;
    private String img2;

    // 🔹 Primary Key (auto increment)
    private Integer id;
}