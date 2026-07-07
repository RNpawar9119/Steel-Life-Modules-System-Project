package com.jwt.tok.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt.tok.model.Login;
import com.jwt.tok.repository.LoginRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private LoginRepository loginRepository;

//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		Login login = loginRepository.findByUsername(username);
//		if (login == null)
//			throw new UsernameNotFoundException("User not found");
//
//		return new org.springframework.security.core.userdetails.User(login.getUsername(), login.getPassword(),
//				new ArrayList<>());
//	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Login login = loginRepository.findByUsername(username);

		if (login == null) {
			throw new UsernameNotFoundException("User not found");
		}

		// ✅ Authorities will come from JwtFilter
		return new org.springframework.security.core.userdetails.User(login.getUsername(), login.getPassword(), true, // enabled
				true, // accountNonExpired
				true, // credentialsNonExpired
				true, // accountNonLocked
				new ArrayList<>() // roles will be injected by JwtFilter
		);
	}

}
