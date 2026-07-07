package com.jwt.tok.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tok.model.Menu;
import com.jwt.tok.model.Role;
import com.jwt.tok.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@PostMapping
	public ResponseEntity<Role> create(@RequestBody Role role) {
		return new ResponseEntity<>(roleService.createRole(role), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@GetMapping
	public ResponseEntity<List<Role>> all() {
		return ResponseEntity.ok(roleService.getAllRoles());
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@GetMapping("/{id}")
	public ResponseEntity<Role> get(@PathVariable Long id) {
		return ResponseEntity.ok(roleService.getRole(id));
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@PutMapping("/{id}")
	public ResponseEntity<Role> update(@PathVariable Long id, @RequestBody Role role) {
		return ResponseEntity.ok(roleService.updateRole(id, role));
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		roleService.deleteRole(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@GetMapping("/{roleId}/menus")
	public ResponseEntity<Set<Menu>> getMenus(@PathVariable Long roleId) {
		return ResponseEntity.ok(roleService.getMenusByRole(roleId));
	}

//	@PostMapping("/{roleId}/assign-menus")
//	public Role assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
//	    return roleService.assignMenus(roleId, menuIds);
//	}
	@PreAuthorize("hasAnyRole('Admin')")
	@PostMapping("/{roleId}/assign-menus")
	public Role assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
		return roleService.assignMenus(roleId, menuIds);
	}

}
