package com.jwt.tok.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Orders;
import com.jwt.tok.model.enums.OrderWorkflowStatus;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
	@Query("SELECT o.id FROM Orders o WHERE o.id LIKE :prefix% ORDER BY o.id DESC")
	List<String> findLastOrderId(@Param("prefix") String prefix);

	long countByStatus(String status);

	List<Orders> findByStatus(String status);

	long countByAssignedDealer_DealerCode(String dealerCode);

	List<Orders> findByDealerCode(String dealerCode);

	long countByDealerCode(String dealerCode);

	List<Orders> findByWorkflowStatus(OrderWorkflowStatus status);
	long countByOrderIdStartingWith(String prefix);
	
	//-----------form date -to date
	List<Orders> findByDateBetween(LocalDate fromDate, LocalDate toDate);
	List<Orders> findByDateBetweenAndDealerCode(LocalDate fromDate, LocalDate toDate, String dealerCode);
	
	List<Orders> findByStatusAndDateBetween(String status, LocalDate from, LocalDate to);  //complated orders
	
	 @Query("SELECT o FROM Orders o WHERE o.status = 'CANCELLED' AND o.date BETWEEN :fromDate AND :toDate")
	    List<Orders> findCancelledOrdersBetweenDates(
	            @Param("fromDate") LocalDate fromDate,
	            @Param("toDate") LocalDate toDate);
	 
	 
	 
}
