package com.healthcare.mapper;

import com.healthcare.dto.RegisterRequest;
import com.healthcare.entities.User;

public class UserMapper {

	public static User toEntity(RegisterRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		return user;
		
	}
}
