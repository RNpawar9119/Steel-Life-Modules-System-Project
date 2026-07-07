package com.jwt.tok.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
public class QuantitySurveyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String type;
    private String grp;
    private String des;
    private String side;
    private String formula;
    private String qty;
  
    private Long materialId;  
    private Long formulaId; 
    private Long sideId;
    
    @ManyToOne
    @JoinColumn(name = "survey_id")
    @JsonBackReference
    private QuantitySurvey survey;}