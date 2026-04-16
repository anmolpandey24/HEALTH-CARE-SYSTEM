package com.healthcare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {
 
	@NotBlank(message = "Username is required")
	@Pattern(
	        regexp = "^[A-Za-z][A-Za-z0-9._]{4,19}$",
	        message = "Username must start with a letter, can contain letters, numbers, underscores, and dots, and be 5-20 characters long"
	    )
	private String username;
	
	//@Email(message = "Invalid email Format")
	@Pattern(regexp="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|org|net|in)$", message = "Email must be in Valid format and must have proper domain")
	@NotBlank(message = "Email is Required")
	private String email;
	
	@NotBlank(message = "Password is Required")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).*$", message = "Password must contain atleast 1 uppercase letter and 1 number")
	private String password;
}
