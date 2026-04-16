package com.healthcare.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate appointmentDate;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Slot slot;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private AppointmentStatus status;
	
	@ManyToOne
	private DiagnosticCenter center;
	
	@ManyToOne
	private DiagnosticTest test;
	
	@ManyToOne
	private User user;
}
