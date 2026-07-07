package com.jwt.tok.model;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "roomtyp")   
@Data
public class AddRoomMS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RID")
    private Long rid;   // auto set

    @Column(name = "DES")
    private String description;
}