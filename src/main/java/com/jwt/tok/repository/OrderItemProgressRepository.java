package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.tok.model.OrderItemProgress;
import com.jwt.tok.model.Orders;

public interface OrderItemProgressRepository extends JpaRepository<OrderItemProgress, Long> {

	List<OrderItemProgress> findByOrderItemId(Long orderItemId);
	long countByOrderItemIdAndStatus(Long orderItemId, String status);

	List<OrderItemProgress> findByOrderItemIdAndSequenceProcessIdOrderByIdDesc(Long orderItemId,
			Long sequenceProcessId);
}