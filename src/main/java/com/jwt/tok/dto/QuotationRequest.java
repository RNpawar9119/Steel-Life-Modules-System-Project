package com.jwt.tok.dto;

import java.util.List;

import com.jwt.tok.model.Slq;
import com.jwt.tok.model.Slq1;
import com.jwt.tok.model.Slq2;

import lombok.Data;

@Data
public class QuotationRequest {

    private Slq header;
    private List<Slq1> items;
    private List<Slq2> details;
}