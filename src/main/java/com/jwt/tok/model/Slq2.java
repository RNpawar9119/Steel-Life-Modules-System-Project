package com.jwt.tok.model;

import lombok.Data;

@Data
public class Slq2 {

    private String srNo;

    private String bomHardware; // btyp
    private String code;
    private String description;

    private String width;
    private String height;
    private String depth;
    private String quantity;

    private String materialValue; // amt1

    private String discCustomer;
    private String discDealer;

    private String customerCash;
    private String customerTax;
    private String dealerCash;
    private String dealerTax;
}