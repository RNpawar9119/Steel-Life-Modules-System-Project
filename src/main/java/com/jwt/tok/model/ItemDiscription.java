//package com.jwt.tok.model;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//import lombok.Data;
//
//@Entity
//@Data
//public class ItemDiscription {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@Column(nullable = false)
//	private String description;
//
//	private String pic;
//}


package com.jwt.tok.model;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "itemdes") 
@Data
public class ItemDiscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 RID (auto generate)
    @Column(name = "RID", unique = true)
    private String rid;

    // 🔥 Description (DES column)
    @Column(name = "DES", nullable = false)
    private String description;

    // 🔥 Image column
    @Column(name = "pic")
    private String pic;
}