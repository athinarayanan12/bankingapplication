package com.security.bank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.security.bank.dto.JwtRequest;
import com.security.bank.dto.JwtResponse;
import com.security.bank.dto.UserDto;
import com.security.bank.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public JwtResponse login(@RequestBody JwtRequest request) {
		return userService.login(request);
	}
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public void createUser(@RequestBody UserDto userDto) {
		userService.createUser(userDto);
	}
}
