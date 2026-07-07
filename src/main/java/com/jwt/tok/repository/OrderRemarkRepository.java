package com.jwt.tok.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.OrderRemark;

@Repository
public interface OrderRemarkRepository extends JpaRepository<OrderRemark, Long> {

    List<OrderRemark> findByOrderIdOrderByCreatedAtDesc(Long orderId);

}