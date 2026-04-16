package com.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DiagnosticTestRequest {

	@NotBlank(message = "Test name is required")
	private String testName;
	private String description;
	
	@NotNull(message = "Price is required")
	@Positive(message = "Price must be greater than 0")
	@JsonProperty("testPrice")
	private Double price;
	
	
}
