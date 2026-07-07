package com.jwt.tok.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Data
@Table(name = "dealer", uniqueConstraints = { @UniqueConstraint(columnNames = "dealerCode"),
		@UniqueConstraint(columnNames = "username") })

public class Dealer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String dealerName;

	private String dealerCode;
	private String address;
	private String mobileNo;

	private String createdate;

	private String bankName;
	private String accountNo;
	private String ifscCode;
	private String gstNo;
	private String state;
	private String remark;

	private String username;
	private String password;

	@Transient
	private String confirmPassword;

	private boolean active = true; // ✅ ADD THIS

}
