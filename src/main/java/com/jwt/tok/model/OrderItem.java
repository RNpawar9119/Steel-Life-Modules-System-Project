package com.jwt.tok.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String room;
	private String product;

	@ManyToOne
	@JoinColumn(name = "sequence_id")
	private Sequence sequence;

	private String doc1;
	private String doc2;
	private String doc3;
	private String doc4;
	private String doc5;

	@ManyToOne
	@JoinColumn(name = "dealer_id")
	@JsonIgnore
	private Dealer dealer;

	@ManyToOne
	@JoinColumn(name = "order_fk")
	@JsonIgnore
	private Orders order;

	@Column(nullable = false)
	private String status; 

	@PrePersist
	public void setDefaultStatus() {
		if (this.status == null) {
			this.status = "PENDING";
		}
	}
}