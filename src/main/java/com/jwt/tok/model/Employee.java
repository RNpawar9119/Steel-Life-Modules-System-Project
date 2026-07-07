
package com.jwt.tok.model;

import javax.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
@Table(
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = "employeeCode"),
	        @UniqueConstraint(columnNames = "username")
	    }
	)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeName;
    private String address;
    private String mobileNo;
    private LocalDate dateOfJoining;
    private LocalDate dateOfBirth;
    private String designation;
    private String departmentName;
    private String bankName;
    private String accountNo;
    private String ifscCode;
    
    private String remark;
    @Column(nullable = false, unique = true)
    private String employeeCode;   
    @Column(unique = true)
    private String username;

    private String password;

    @Transient
    private String confirmPassword;
    
    @Column(nullable = false)
    private boolean active = true;
}
