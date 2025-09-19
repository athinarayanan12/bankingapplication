package com.security.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.security.bank.dto.JwtRequest;
import com.security.bank.dto.JwtResponse;
import com.security.bank.dto.UserDto;
import com.security.bank.entity.Role;
import com.security.bank.entity.User;
import com.security.bank.jwt.JwtAuthenticationHelper;
import com.security.bank.repository.UserRepository;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	@Autowired
	private AuthenticationManager manager; 
	@Autowired
	private JwtAuthenticationHelper helper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private Counter counter=null;
	public UserService(CompositeMeterRegistry compositeMeterRegistry ) {
		this.counter=compositeMeterRegistry.counter("user.login.count");
	}
	public JwtResponse login(JwtRequest request) {
		this.doAuthentication(request.getUsername(),request.getPassword());
		UserDetails userDetails =userDetailsService.loadUserByUsername(request.getUsername());
		String token =helper.generateToken(userDetails);
		
		log.info("User logged in successfully.");
		counter.increment();
		return JwtResponse.builder().jwtToken(token).build();
	}

	private void doAuthentication(String username, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		try {
			manager.authenticate(authenticationToken);

		}catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Username or Password");
		}
		
	}

	public void createUser(UserDto userDto) {
		Role role = Role.builder().roleName("ROLE_CUSTOMER").build();
		User user =User.builder().name(userDto.getName()).number(userDto.getNumber()).address(userDto.getAddress())
					.password(passwordEncoder.encode(userDto.getPassword())).username(userDto.getUsername()).roles(role)
					.identityProof(userDto.getIdentityProof())  // ADDED
					.build();
		
		userRepository.save(user);
		log.info("User created successfully.");
	}

	
}
