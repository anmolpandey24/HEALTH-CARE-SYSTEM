package com.healthcare.dto;

import java.time.LocalDate;

import com.healthcare.entities.Slot;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AppointmentRequest {

	@NotNull(message = "center Id is required")
	@Positive(message = "center id must be positive")
	private Long centerId;
	
	@NotNull(message = "test Id is required")
	@Positive(message = "Test id must be positive")
	private Long testId;
	
	@NotNull(message = "Appointment date is required")
	@FutureOrPresent(message = "Appointment date cannot be in past")
	private LocalDate appointmentDate;
	@NotNull(message = "Slot is required")
	private Slot slot;
}
