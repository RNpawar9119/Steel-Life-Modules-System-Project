package com.jwt.tok.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "sequence_process")
@Data
public class SequenceProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seq_no")
    private Integer seqNo;

    @Column(name = "process_name", nullable = false)
    private String processName;

    @ManyToOne
    @JoinColumn(name = "sequence_id")
    @JsonBackReference
    private Sequence sequence;
}
