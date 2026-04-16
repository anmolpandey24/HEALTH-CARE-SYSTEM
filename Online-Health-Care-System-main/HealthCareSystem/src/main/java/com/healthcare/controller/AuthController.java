package com.healthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.ChangePasswordRequest;
import com.healthcare.dto.LoginRequest;
import com.healthcare.dto.RegisterRequest;
import com.healthcare.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

	@Autowired
	AuthService authservice;
	
	@PostMapping("/register-user")
	public ApiResponse<?> registerUser(@Valid @RequestBody RegisterRequest request){
		
		return authservice.registerUser(request);
		
	}
	
	@PostMapping("/register-admin")
	public ApiResponse<?> registerAdmin( @Valid @RequestBody RegisterRequest request){
		
		return authservice.registerAdmin(request);
		
	}
	
	@PostMapping("/login")
	public ApiResponse<?> login(@RequestBody LoginRequest request){
		
		return authservice.login(request);
		
	}

	@PostMapping("/change-password")
	public ApiResponse<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		return authservice.changePassword(request);
	}
	
	
	
}
