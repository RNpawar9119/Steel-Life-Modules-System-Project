package com.jwt.tok.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class OrderItemProgress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String status;

	private LocalDateTime startTime;
	private LocalDateTime stopTime;
	private LocalDateTime completedTime;

	@ManyToOne
	@JoinColumn(name = "order_item_id")
	private OrderItem orderItem;
	@ManyToOne
	@JoinColumn(name = "sequence_process_id")
	private SequenceProcess sequenceProcess;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee startedBy;
}