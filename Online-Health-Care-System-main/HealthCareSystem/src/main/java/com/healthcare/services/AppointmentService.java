package com.healthcare.services;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.AppointmentRequest;

public interface AppointmentService {

	ApiResponse<?> bookAppointment(AppointmentRequest request, String username);
	
	ApiResponse<?> getMyAppointments(String username);
	ApiResponse<?> cancelAppointment(Long id, String username);
	ApiResponse<?> getCenterAppointments(String username);
	ApiResponse<?> updateStatus(Long id, String status, String username); // this is for the admin 
}
