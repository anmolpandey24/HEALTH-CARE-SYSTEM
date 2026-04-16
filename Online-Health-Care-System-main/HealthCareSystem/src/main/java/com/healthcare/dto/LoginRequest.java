package com.healthcare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NotBlank
@Data
public class LoginRequest {
	 
	@NotBlank(message = "Username is required")
	@Size(min=3, max=20, message="Username must be 3-20 characters")
	@Pattern(
	        regexp = "^[A-Za-z][A-Za-z0-9._]{4,19}$",
	        message = "Username must start with a letter, can contain letters, numbers, underscores, and dots, and be 5-20 characters long"
	    )
		private String username;

		@NotBlank
		@Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).*$", message = "Password must contain atleast 1 uppercase letter and 1 number")
		private String password;
	}


