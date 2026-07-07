package com.jwt.tok.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "formulahead") 
@Data
public class FormulaHead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RID")
    private String rid;

    @Column(name = "DES", nullable = false)
    private String description;
}