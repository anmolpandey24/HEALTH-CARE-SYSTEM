package com.healthcare.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.entities.Appointment;
import com.healthcare.entities.DiagnosticTest;
import com.healthcare.entities.Slot;
import com.healthcare.entities.User;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	long countByCenterIdAndTestIdAndAppointmentDateAndSlot( Long centerId, Long testId, LocalDate date, Slot slot);
	
	List<Appointment> findByUserId(Long userId);
	boolean existsByUserAndTestAndAppointmentDateAndSlot(User user, DiagnosticTest test, LocalDate appointment, Slot slot);
	List<Appointment> findByCenterId(Long centerId);
	
	boolean existsByUserIdAndCenterIdAndTestIdAndAppointmentDate(Long userId, Long centerId, Long testId, LocalDate appointmentDate);
}
