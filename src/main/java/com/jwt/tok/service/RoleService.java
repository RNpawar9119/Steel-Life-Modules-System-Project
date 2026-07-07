package com.jwt.tok.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.jwt.tok.model.Menu;
import com.jwt.tok.model.Role;
import com.jwt.tok.repository.MenuRepository;
import com.jwt.tok.repository.RoleRepository;

@Service
@Transactional
public class RoleService {

	private final RoleRepository roleRepo;
	private final MenuRepository menuRepo;

	public RoleService(RoleRepository roleRepo, MenuRepository menuRepo) {
		this.roleRepo = roleRepo;
		this.menuRepo = menuRepo;
	}

	public Role createRole(Role role) {
		return roleRepo.save(role);
	}

	public List<Role> getAllRoles() {
		return roleRepo.findAll();
	}

	public Role getRole(Long id) {
		return roleRepo.findById(id).orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
	}

	public Role updateRole(Long id, Role role) {
		Role dbRole = getRole(id);

		dbRole.setRole(role.getRole());
		dbRole.setDescription(role.getDescription());

		return roleRepo.save(dbRole);
	}

	public void deleteRole(Long id) {
		if (!roleRepo.existsById(id)) {
			throw new RuntimeException("Role not found with id: " + id);
		}
		roleRepo.deleteById(id);
	}

	public Set<Menu> getMenusByRole(Long roleId) {
		return getRole(roleId).getMenus();
	}

//	public Role assignMenus(Long roleId, List<Long> menuIds) {
//		Role role = getRole(roleId);
//		List<Menu> menuList = menuRepo.findAllById(menuIds);
//		if (menuList.isEmpty())
//			throw new RuntimeException("No valid menus found");
//		role.setMenus(new HashSet<>(menuList));
//		return roleRepo.save(role);
//	}
//	public Role assignMenus(Long roleId, List<Long> menuIds) {
//	    Role role = getRole(roleId);
//
//	    List<Menu> menus = menuRepo.findAllById(menuIds);
//	    role.setMenus(new HashSet<>(menus));
//
//	    return roleRepo.save(role);
//	}
	public Role assignMenus(Long roleId, List<Long> menuIds) {
	    Role role = getRole(roleId);

	    if (menuIds == null) {
	        role.setMenus(new HashSet<>());
	        return roleRepo.save(role);
	    }

	    List<Menu> menus = menuRepo.findAllById(menuIds);
	    role.setMenus(new HashSet<>(menus));

	    return roleRepo.save(role);
	}


}
