package com.jwt.tok.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Dealer;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {
	boolean existsByDealerCode(String dealerCode);

	boolean existsByDealerCodeAndIdNot(String dealerCode, Long id);
	long count();

	Optional<Dealer> findByDealerCode(String dealerCode);
	
	 List<Dealer> findByDealerNameContainingIgnoreCase(String dealerName);

	    
	 Optional<Dealer> findByUsername(String username);
	 
	 List<Dealer> findByDealerNameStartingWithIgnoreCase(String dealerName);
}
