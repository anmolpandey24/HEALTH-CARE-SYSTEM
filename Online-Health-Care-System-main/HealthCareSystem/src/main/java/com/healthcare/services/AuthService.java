package com.healthcare.services;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.ChangePasswordRequest;
import com.healthcare.dto.LoginRequest;
import com.healthcare.dto.RegisterRequest;

public interface AuthService {

	ApiResponse<?> registerUser(RegisterRequest request);
	ApiResponse<?> registerAdmin(RegisterRequest request);

	ApiResponse<?> login(LoginRequest request);

	ApiResponse<?> changePassword(ChangePasswordRequest request);

}
