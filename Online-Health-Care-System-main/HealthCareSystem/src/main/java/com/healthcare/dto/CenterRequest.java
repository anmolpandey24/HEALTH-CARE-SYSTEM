package com.healthcare.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CenterRequest {

	private String ownerName;
	
	@NotBlank(message = "CenterName is required")
	private String centerName;
	
	@NotBlank(message = "Address is required")
	private String address;
}
