
package com.jwt.tok.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tok.model.Employee;
import com.jwt.tok.model.Login;
import com.jwt.tok.repository.EmployeeRepository;
import com.jwt.tok.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	private final EmployeeService service;
	@Autowired
	private EmployeeRepository employeeRepository;

	public EmployeeController(EmployeeService service) {
		this.service = service;
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@PostMapping("/create")
	public ResponseEntity<Employee> create(@RequestBody Employee employee) {
		return new ResponseEntity<>(service.create(employee), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	@GetMapping("/all")
	public List<Employee> all() {
		return service.getAll();
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@GetMapping("/{id}")
	public Employee get(@PathVariable Long id) {
		return service.getById(id);
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@PutMapping("/{id}")
	public Employee update(@PathVariable Long id, @RequestBody Employee emp) {
		return service.update(id, emp);
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}

	@GetMapping("/search")
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String q) {
		return new ResponseEntity<>(
				employeeRepository.findByEmployeeNameContainingIgnoreCaseOrEmployeeCodeContainingIgnoreCase(q, q),
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@PutMapping("/toggle-status/{id}")
	public ResponseEntity<String> toggleEmployeeStatus(@PathVariable Long id) {

		Employee emp = service.getById(id);

		boolean newStatus = !emp.isActive();
		emp.setActive(newStatus);
		employeeRepository.save(emp);

		// 🔥 LOGIN TABLE UPDATE
		Login login = service.getLoginByUsername(emp.getUsername());
		if (login != null) {
			login.setActive(newStatus);
			service.saveLogin(login);
		}

		return ResponseEntity.ok(newStatus ? "Employee Activated" : "Employee Deactivated");
	}

	@PreAuthorize("hasRole('Employee')")
	@PutMapping("/change-credentials")
	public ResponseEntity<String> changeCredentials(@RequestBody Employee employee, Authentication authentication) {

		String username = authentication.getName(); // logged-in user

		service.changeCredentials(username, employee);

		return ResponseEntity.ok("Credentials updated successfully");
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	@GetMapping("/auto-search")
	public ResponseEntity<List<Employee>> autoSearch(
	        @RequestParam(required = false, defaultValue = "") String keyword) {

	    return ResponseEntity.ok(service.autoSearch(keyword));
	}
}
