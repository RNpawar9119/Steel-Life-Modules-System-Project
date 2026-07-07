package com.jwt.tok.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jwt.tok.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	// 🔥 Search by productName containing (ignore case)
	List<Product> findByProductCodeContainingIgnoreCase(String productCode);

	boolean existsByProductCode(String productCode);

	boolean existsByProductCodeAndIdNot(String productCode, Long id);

	List<Product> findByProductCodeContainingIgnoreCaseOrProductNameContainingIgnoreCase(String productCode,
			String productCode2);

	Product findTopByOrderByIdDesc();

	@Query("SELECT new map(" + " p.id as id," + " p.productName as productName," + " p.productCode as productCode,"
			+ " p.tax as tax," + " p.unit as unit," + " h.hnsCode as hnsCode" + ") " + "FROM Product p "
			+ "JOIN p.hns h " + "WHERE " + " (LOWER(p.productName) LIKE LOWER(CONCAT('%', :q, '%')) "
			+ "  OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :q, '%'))) ")
	List<Map<String, Object>> searchProductLite(@Param("q") String q);

}
