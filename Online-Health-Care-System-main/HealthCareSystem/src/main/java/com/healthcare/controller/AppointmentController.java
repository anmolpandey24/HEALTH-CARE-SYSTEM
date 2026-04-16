package com.healthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.AppointmentRequest;
import com.healthcare.services.AppointmentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

	@Autowired
	AppointmentService service;
	
	@PostMapping
	public ApiResponse<?> book (@RequestBody AppointmentRequest request, HttpServletRequest req) {
		return service.bookAppointment(request, (String) req.getAttribute("username"));
	}
	
	@GetMapping("/my")
	public ApiResponse<?> my(HttpServletRequest req) {
		return service.getMyAppointments((String) req.getAttribute("username"));
	}
	
	
	@PutMapping("/{id}/status")
	public ApiResponse<?> updateStatus(@PathVariable Long id, @RequestParam String status, HttpServletRequest req){
		return service.updateStatus(id, status, (String) req.getAttribute("username"));
	}
	
	@DeleteMapping("/{id}")
	public ApiResponse<?> cancel(@PathVariable Long id, HttpServletRequest req) {
		return service.cancelAppointment(id, (String) req.getAttribute("username"));
	}
}
