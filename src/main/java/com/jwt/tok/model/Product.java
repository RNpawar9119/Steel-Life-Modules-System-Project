package com.jwt.tok.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String productCode;

	@Column(nullable = false)
	private String productName;

	private String modelName;

	@Column(length = 1000)
	private String description;

	private Double tax;
	private Double unit;
	private String type;

	private Double salesPrice;
	private Double dealerPrice;
	private Double buyPrice;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hns_id", nullable = false)
	private HNSMaster hns;

	private String image1;
	private String image2;
	private String image3;
}
