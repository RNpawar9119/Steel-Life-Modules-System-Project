package com.jwt.tok.model;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "room_master")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomNo;

    @Column(nullable = false)
    private String roomName;
}
