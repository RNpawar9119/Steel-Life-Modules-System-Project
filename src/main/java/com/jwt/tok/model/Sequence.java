package com.jwt.tok.model;

import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "sequence")
@Data
public class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sequence_name", nullable = false)
    private String sequenceName;

    @OneToMany(mappedBy = "sequence",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @JsonManagedReference
    private List<SequenceProcess> processes;
}
