package com.jwt.tok.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tok.dto.ChangePasswordRequest;
import com.jwt.tok.model.Dealer;
import com.jwt.tok.model.Employee;
import com.jwt.tok.model.Login;
import com.jwt.tok.model.Menu;
import com.jwt.tok.repository.DealerRepository;
import com.jwt.tok.repository.EmployeeRepository;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthService authService;

	@Autowired
	private DealerRepository dealerRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Object>> register(@RequestBody Login login) {
		authService.registerUser(login);
		return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Object>> login(@RequestBody Map<String, String> req) {

		String username = req.get("username");
		String password = req.get("password");

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		Login user = authService.getLoginByUsername(username);

//		if (!user.isActive()) {
//		    return ResponseEntity.badRequest().body(ApiResponse.fail("User is deactivated. Contact Admin."));
//		}

		String roleName = user.getRole() != null ? user.getRole().getRole() : "NO_ROLE";

		// 🔥 Admin always allowed
		if (!"Admin".equals(roleName) && !user.isActive()) {
		    return ResponseEntity.badRequest()
		        .body(ApiResponse.fail("User is deactivated. Contact Admin."));
		}

		String token = authService.generateTokenWithUser(user);

		Map<String, Object> response = new HashMap<>();
		response.put("id", user.getId());
		response.put("username", user.getUsername());
		response.put("role", roleName);
		response.put("token", token);

		if ("Dealer".equals(roleName)) {
			Dealer dealer = dealerRepository.findByUsername(user.getUsername()).orElse(null);

			response.put("dealerCode", dealer != null ? dealer.getDealerCode() : null);
		}

		if ("Employee".equals(roleName)) {
			Employee emp = employeeRepository.findByUsername(user.getUsername()).orElse(null);

			if (emp != null) {
				response.put("employeeId", emp.getId()); // 🔥 VERY IMPORTANT
				response.put("employeeCode", emp.getEmployeeCode());
			}
		}

		return ResponseEntity.ok(ApiResponse.success("Login successful", response));
	}

	@GetMapping("/my-menus")
	public ResponseEntity<Set<Menu>> myMenus(@RequestHeader("Authorization") String token) {

		Login user = authService.validateTokenAndGetUser(token);

		if (user.getRole() == null) {
			return ResponseEntity.ok(Collections.emptySet());

		}

		return ResponseEntity.ok(user.getRole().getMenus());
	}

	@PutMapping("/assign-role/{userId}/{roleId}")
	public ResponseEntity<String> assignRole(@PathVariable Long userId, @PathVariable Long roleId) {

		authService.assignRoleToUser(userId, roleId);
		return ResponseEntity.ok("Role assigned successfully");
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<ApiResponse<Login>> getUserById(@PathVariable Long id) {

		Login user = authService.getUserById(id);
		return ResponseEntity.ok(ApiResponse.success("User fetched successfully", user));
	}

	@GetMapping("/users")
	public ResponseEntity<ApiResponse<Object>> getAllUsers() {

		return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", authService.getAllUsers()));
	}
	
	@PutMapping("/change-credentials")
	public ResponseEntity<ApiResponse<String>> changeCredentials(
	        @RequestBody Map<String, String> req,
	        @RequestHeader("Authorization") String token) {

	    authService.changeCredentials(req, token);

	    return ResponseEntity.ok(ApiResponse.success("Credentials updated successfully"));
	}	
	
//	@PutMapping("/change-password")
//	public ResponseEntity<?> changePassword(
//	        @RequestHeader("Authorization") String token,
//	        @RequestBody Map<String, String> req
//	) {
//	    authService.changePassword(token, req.get("newPassword"), req.get("confirmPassword"));
//	    return ResponseEntity.ok("Password changed successfully");
//	}
	@PutMapping("/change-password")
	public ResponseEntity<?> changePassword(
	        @RequestHeader("Authorization") String token,
	        @RequestBody Map<String, Object> req
	) {
	    authService.changePassword(
	        token,
	        req.get("userId") != null ? Long.parseLong(req.get("userId").toString()) : null,
	        req.get("newPassword").toString(),
	        req.get("confirmPassword").toString()
	    );

	    return ResponseEntity.ok("Password changed successfully");
	}	
}
