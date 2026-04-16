package com.healthcare.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.*;
@AllArgsConstructor
@NotBlank
@Data
public class ApiResponse<T>{

	private boolean success;
	private String message;
	private T data;
	
}
