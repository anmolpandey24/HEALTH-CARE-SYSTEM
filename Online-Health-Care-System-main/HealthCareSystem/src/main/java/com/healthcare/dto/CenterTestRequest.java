package com.healthcare.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CenterTestRequest {

	@NotNull(message = "test Id is required")
	@Positive(message = "Test id must be positive")
	private Long testId;
	
	@NotNull(message = "Max Booking is required")
	@Min(value = 1, message = "Max Booking must be atleast 1")
	private Integer maxBooking;
	
}
