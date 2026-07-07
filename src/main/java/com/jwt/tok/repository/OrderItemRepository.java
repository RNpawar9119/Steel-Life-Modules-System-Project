package com.jwt.tok.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("select oi.id, oi.room, oi.product, d.dealerName, d.dealerCode\r\n"
			+ "		    from OrderItem oi\r\n"
			+ "		    join oi.dealer d\r\n"
			+ "		    where d.username = :username")
		List<Object[]> findAssignedOrdersByDealerUsername(@Param("username") String username);

    @Query("select oi.id, oi.room, oi.product, d.dealerName, d.dealerCode\r\n"
    		+ "    	    from OrderItem oi\r\n"
    		+ "    	    join oi.dealer d")
    	List<Object[]> findAssignedOrders();

}
