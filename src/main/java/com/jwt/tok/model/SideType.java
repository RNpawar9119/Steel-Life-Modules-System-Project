//package com.jwt.tok.model;
//
//import javax.persistence.*;
//
//import lombok.Data;
//
//@Entity
//@Data
//public class SideType {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String description;
//}


package com.jwt.tok.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sidetyp") // 🔥 DB table name match
@Data
public class SideType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 RID column (auto generate)
    @Column(name = "RID", unique = true)
    private String rid;

    // 🔥 Description column (DES)
    @Column(name = "DES", nullable = false)
    private String description;
}