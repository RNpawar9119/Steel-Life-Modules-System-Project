package com.jwt.tok.model;

import lombok.Data;

@Data
public class Slq {
    private String fyear;
    private String doctype;
    private String docno;
    private String docdt;

    private String t0; // name
    private String t1; // 1mobile
    private String t2; // 2 mobile
    private String t3; // email
    private String t4; // address
    private String t5; // type

    private String t6; // created by
    private String t7; // quotation type
    private String t8;
    private String t9;

    private String famt;
}