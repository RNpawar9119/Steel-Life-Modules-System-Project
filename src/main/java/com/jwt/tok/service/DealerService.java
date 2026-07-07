package com.jwt.tok.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.Dealer;
import com.jwt.tok.model.Login;
import com.jwt.tok.model.Role;
import com.jwt.tok.repository.DealerRepository;
import com.jwt.tok.repository.LoginRepository;
import com.jwt.tok.repository.RoleRepository;

@Service
public class DealerService {

	private final DealerRepository repo;
	private final LoginRepository loginRepo;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public DealerService(DealerRepository repo, LoginRepository loginRepo, RoleRepository roleRepository) {
		this.repo = repo;
		this.loginRepo = loginRepo;
		this.roleRepository = roleRepository;
	}

	public Dealer create(Dealer dealer) {

		// username unique
//	    if (loginRepo.findByUsername(dealer.getUsername()) != null) {
//	        throw new ApiException("Username already exists");
//	    }

		if (!dealer.getPassword().equals(dealer.getConfirmPassword())) {
			throw new ApiException("Passwords do not match");
		}

		String encoded = encoder.encode(dealer.getPassword());
		dealer.setPassword(encoded);
		dealer.setActive(true); // ✅ ADD

		String nextCode = previewDealerCode(); // e.g., SL/DLR/001
		dealer.setDealerCode(nextCode);
		Dealer savedDealer = repo.save(dealer);

		// 🔐 Login entry
		Login login = new Login();

		try {
			Role dealerRole = roleRepository.findByRole("Dealer");

			login.setRole(dealerRole);
		} catch (Exception e) {
			System.out.println("Role not found" + e.getMessage());
		}

		login.setUsername(dealer.getUsername());
		login.setPassword(encoded);
		login.setActive(true); // ✅ ADD

		loginRepo.save(login);

		return savedDealer;
	}

	public Dealer getByUsername(String username) {
		return repo.findByUsername(username).orElseThrow(() -> new ApiException("Dealer not found"));
	}

	public List<Dealer> getAll() {
		return repo.findAll();
	}

	/* ================= GET BY ID ================= */
	public Dealer getById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ApiException("Dealer not found with id: " + id));
	}

	/* ================= UPDATE ================= */

//	public Dealer update(Long id, Dealer dealer) {
//
//		Dealer existing = getById(id);
//
//		// 🔹 Duplicate dealerCode check
//		if (repo.existsByDealerCodeAndIdNot(dealer.getDealerCode(), id)) {
//			throw new ApiException("Dealer code already exists ❌");
//		}
//
//		// 🔹 Dealer basic info
//		existing.setDealerCode(dealer.getDealerCode());
//		existing.setDealerName(dealer.getDealerName());
//		existing.setAddress(dealer.getAddress());
//		existing.setMobileNo(dealer.getMobileNo());
//		// existing.setDateOfJoining(dealer.getDateOfJoining());
//		existing.setCreatedate(dealer.getCreatedate());
//		existing.setBankName(dealer.getBankName());
//		existing.setAccountNo(dealer.getAccountNo());
//		existing.setIfscCode(dealer.getIfscCode());
//		existing.setGstNo(dealer.getGstNo());
//		existing.setState(dealer.getState());
//		existing.setRemark(dealer.getRemark());
//
//		// 🔹 Username update
//		if (!existing.getUsername().equals(dealer.getUsername())) {
//
//			if (loginRepo.findByUsername(dealer.getUsername()) != null) {
//				throw new ApiException("Username already exists ❌");
//			}
//
//			existing.setUsername(dealer.getUsername());
//		}
//
//		// 🔹 Password update (ONLY if provided)
//		if (dealer.getPassword() != null && !dealer.getPassword().isEmpty()) {
//
//			String encoded = encoder.encode(dealer.getPassword());
//			existing.setPassword(encoded);
//
//			// 🔥 update login table password
//			Login login = loginRepo.findByUsername(existing.getUsername());
//			login.setPassword(encoded);
//			loginRepo.save(login);
//		}
//
//		return repo.save(existing);
//	}
	public Dealer update(Long id, Dealer dealer) {

	    Dealer existing = getById(id);

	    // Duplicate code check
	    if (repo.existsByDealerCodeAndIdNot(dealer.getDealerCode(), id)) {
	        throw new ApiException("Dealer code already exists ❌");
	    }

	    // Basic fields
	    existing.setDealerCode(dealer.getDealerCode());
	    existing.setDealerName(dealer.getDealerName());
	    existing.setAddress(dealer.getAddress());
	    existing.setMobileNo(dealer.getMobileNo());
	    existing.setCreatedate(dealer.getCreatedate());
	    existing.setBankName(dealer.getBankName());
	    existing.setAccountNo(dealer.getAccountNo());
	    existing.setIfscCode(dealer.getIfscCode());
	    existing.setGstNo(dealer.getGstNo());
	    existing.setState(dealer.getState());
	    existing.setRemark(dealer.getRemark());

	    // USERNAME UPDATE
	    if (!existing.getUsername().equals(dealer.getUsername())) {

	        if (loginRepo.findByUsername(dealer.getUsername()) != null) {
	            throw new ApiException("Username already exists ❌");
	        }

	        Login login = loginRepo.findByUsername(existing.getUsername());

	        if (login != null) {
	            login.setUsername(dealer.getUsername());
	            loginRepo.save(login);
	        }

	        existing.setUsername(dealer.getUsername());
	    }

	    // PASSWORD UPDATE
	    if (dealer.getPassword() != null && !dealer.getPassword().isEmpty()) {

	        String encoded = encoder.encode(dealer.getPassword());
	        existing.setPassword(encoded);

	        Login login = loginRepo.findByUsername(existing.getUsername());

	        if (login != null) {
	            login.setPassword(encoded);
	            loginRepo.save(login);
	        } else {
	            throw new ApiException("Login record not found ❌");
	        }
	    }

	    return repo.save(existing);
	}
	/* ================= DELETE ================= */
	public void delete(Long id) {
		if (!repo.existsById(id)) {
			throw new ApiException("Dealer not found with id: " + id);
		}
		repo.deleteById(id);
	}

	public List<Dealer> searchByName(String name) {
		return repo.findByDealerNameContainingIgnoreCase(name);
	}

	public List<Dealer> searchByNamePrefix(String prefix) {

		if (prefix == null || prefix.trim().isEmpty()) {
			return Collections.emptyList();
		}

		return repo.findByDealerNameStartingWithIgnoreCase(prefix);
	}

	public String previewDealerCode() {
		long count = repo.count() + 1;
		return String.format("SL/DLR/%03d", count + 1);
	}

	public void toggleStatus(Long id) {

		Dealer dealer = repo.findById(id).orElseThrow(() -> new ApiException("Dealer not found"));

		// 🔄 toggle
		dealer.setActive(!dealer.isActive());

		repo.save(dealer);

		// 🔥 LOGIN TABLE ALSO UPDATE
		Login login = loginRepo.findByUsername(dealer.getUsername());

		if (login != null) {
			login.setActive(dealer.isActive());
			loginRepo.save(login);
		}
	}
	
	public void changeCredentials(String username, Dealer dealer) {

	    Dealer existing = repo.findByUsername(username)
	            .orElseThrow(() -> new ApiException("Dealer not found"));

	    Login login = loginRepo.findByUsername(username);

	    if (login == null) {
	        throw new ApiException("Login record not found");
	    }

	    // ✅ CURRENT PASSWORD CHECK
	    if (!encoder.matches(dealer.getPassword(), existing.getPassword())) {
	        throw new ApiException("Current password is incorrect ❌");
	    }

	    // ✅ USERNAME UPDATE
	    if (dealer.getUsername() != null && !dealer.getUsername().equals(username)) {

	        if (loginRepo.findByUsername(dealer.getUsername()) != null) {
	            throw new ApiException("Username already exists ❌");
	        }

	        login.setUsername(dealer.getUsername());
	        existing.setUsername(dealer.getUsername());
	    }

	    // ✅ NEW PASSWORD UPDATE
	    if (dealer.getConfirmPassword() != null && !dealer.getConfirmPassword().isEmpty()) {

	        String encoded = encoder.encode(dealer.getConfirmPassword());

	        login.setPassword(encoded);
	        existing.setPassword(encoded);
	    }

	    loginRepo.save(login);
	    repo.save(existing);
	}

}
