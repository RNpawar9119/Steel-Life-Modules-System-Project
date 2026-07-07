package com.jwt.tok.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jwt.tok.model.enums.OrderWorkflowStatus;

import lombok.Data;

@Entity
@Data
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id", unique = true, nullable = false)
	private String orderId;

	private String partyName;
	private String contactNo;
	private String customerName;
	private String address;
	private String email;
	private String city;
	
	
	@Column(name = "enquiry_ids")
	private String enquiryIds;
	
	@Column(name = "product_name")
	private String productName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "date")
	private LocalDate date;
	// private String date;
	@Column(name = "enquiry_form")
	private String enquiryForm;

	@Column(name = "finalization_report")
	private String finalizationReport;

	@Column(name = "actual_dimension")
	private String actualDimension;

	@Column(name = "design_2d_3d")
	private String design2D3D;
	@Column(nullable = false)
	private String status; 

	@Enumerated(EnumType.STRING)
	@Column(name = "workflow_status", nullable = false)
	private OrderWorkflowStatus workflowStatus;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("order")
	private List<OrderRemark> remarks;

	 @Column(name="enquiry_id",nullable=false)
	    private Long enquiryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "oenquiry_id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Enquiry enquiry;
	
	@ManyToOne
	@JoinColumn(name = "dealer_id")
	private Dealer assignedDealer;

	@Column(name = "dealer_code", nullable = false)
	private String dealerCode;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("order")
	private List<OrderItem> items;

	@PrePersist
	public void setDefaultStatus() {
		if (this.status == null) {
			this.status = "ORDER_BOOKED";
		}
		if (this.workflowStatus == null) {
			this.workflowStatus = OrderWorkflowStatus.DRAFT;
		}
	}
}
