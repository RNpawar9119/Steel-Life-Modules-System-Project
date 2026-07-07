
package com.jwt.tok.repository;

import com.jwt.tok.model.Employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	 long count();

	    List<Employee> findByEmployeeNameContainingIgnoreCaseOrEmployeeCodeContainingIgnoreCase(
	        String name, String code
	    );
	    Optional<Employee> findByUsername(String username);
	    
	    List<Employee> findTop10ByEmployeeNameContainingIgnoreCaseOrEmployeeCodeContainingIgnoreCase(
	    	    String name, String code
	    	);

}