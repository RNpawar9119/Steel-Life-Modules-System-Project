package com.jwt.tok.model;

import java.time.LocalDateTime;

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
public class OrderRemark {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 2000)
	private String remark;

	@Column(nullable = false)
	private String createdBy; // Dealer username
	
	@Column(nullable = false)
	private String role; // ADMIN / DEALER

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonIgnore
	private Orders order;

	@PrePersist
	public void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
	}
}