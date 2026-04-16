package com.healthcare.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.healthcare.config.JwtUtil;
import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.ChangePasswordRequest;
import com.healthcare.dto.LoginRequest;
import com.healthcare.dto.LoginResponse;
import com.healthcare.dto.RegisterRequest;
import com.healthcare.entities.User;
import com.healthcare.exceptions.BadRequestException;
import com.healthcare.mapper.UserMapper;
import com.healthcare.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	public ApiResponse<?> registerUser(RegisterRequest request) {
		if(userRepo.findByUsername(request.getUsername()).isPresent()) {
			throw new BadRequestException("Username already exists");
		}
		
		if(userRepo.findByEmail(request.getUsername()).isPresent()) {
			throw new BadRequestException("Username already exists");
		}
		User user = UserMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole("USER");
		userRepo.save(user);
		return new ApiResponse<>(true, "User registered succesfully", null);
		
	}

	@Override
	public ApiResponse<?> registerAdmin(RegisterRequest request) {
		if(userRepo.findByUsername(request.getUsername()).isPresent()) {
			throw new BadRequestException("Username already exists");
		}
		
		if(userRepo.findByEmail(request.getUsername()).isPresent()) {
			throw new BadRequestException("Username already exists");
		}
		
		User user = UserMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setRole("ADMIN");
		userRepo.save(user);
		return new ApiResponse<>(true, "Admin registered successfully", null);
	}
	

	@Override
	public ApiResponse<?> login(LoginRequest request) {
		User user = userRepo.findByUsername(request.getUsername()).orElseThrow(() ->new BadRequestException("Invalid username or password") );
		if(! passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadRequestException("Invalid username or password");
		}
		
		if(!user.isActive()) {
			System.out.println("Login attempt by blocked user: " + user.getUsername());
			return new ApiResponse<>(false, "Your account has been blocked by the Super Admin. Please contact support.", user.getUsername());
		}
		String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
		LoginResponse loginResponse = new LoginResponse(token, user.getRole(), user.getUsername());
		return new ApiResponse<>(true, "Login Successfull", loginResponse);
	}

	@Override
	public ApiResponse<?> changePassword(ChangePasswordRequest request) {
		if (!request.getNewPassword().equals(request.getConfirmPassword())) {
			throw new BadRequestException("New password and confirm password do not match");
		}

		User user = userRepo.findByUsername(request.getUsername())
				.orElseThrow(() -> new BadRequestException("Invalid username or password"));

		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new BadRequestException("Current password is incorrect");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepo.save(user);
		return new ApiResponse<>(true, "Password updated successfully", null);
	}

}
