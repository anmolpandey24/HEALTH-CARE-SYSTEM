package com.healthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.CenterRequest;

import jakarta.validation.Valid;
import com.healthcare.dto.CenterTestRequest;
import com.healthcare.services.AppointmentService;
import com.healthcare.services.DiagnosticCenterService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/centers")

@CrossOrigin(
	origins = "http://localhost:4200",
	allowedHeaders = "*",
	methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class DiagnosticCenterController {

	@Autowired
	DiagnosticCenterService service;
	@Autowired
	AppointmentService appointmentService;
	
	@PostMapping("/request")
	public ApiResponse<?> request(@Valid @RequestBody CenterRequest req, HttpServletRequest http) {
		return service.requestCenter(req, (String) http.getAttribute("username"));
	}

	@GetMapping("/approved")
	public ApiResponse<?> getApprovedCenters() {
		return service.getApprovedCenters();
	}

	@GetMapping("/me")
	public ApiResponse<?> getMyCenter(HttpServletRequest http) {
		return service.getMyCenter((String) http.getAttribute("username"));
	}
	
	//@PostMapping("/{centerId}/add-test/{testId}")
//	public ApiResponse<?> add(@PathVariable Long centerId, @PathVariable Long testId,HttpServletRequest http ) {
	@PostMapping("/{centerId}/add-test")
	public ApiResponse<?> add(@PathVariable Long centerId,@RequestBody CenterTestRequest request,HttpServletRequest http ) {
		return service.addTest(centerId, request, (String) http.getAttribute("username"));
	}
	
	@DeleteMapping("/{centerId}/remove-test/{testId}")
	public ApiResponse<?> remove(@PathVariable Long centerId, @PathVariable Long testId,HttpServletRequest http) {
		return service.removeTest(centerId, testId, (String) http.getAttribute("username"));
	}
	
	@GetMapping("/{centerId}/tests")
	public ApiResponse<?> tests(@PathVariable Long centerId) {
		System.out.println("hello");
		return service.getCenterTests(centerId);
	}
	
	@GetMapping("/appointments")
	public ApiResponse<?> getAppointments(HttpServletRequest req) {
		return appointmentService.getCenterAppointments((String) req.getAttribute("username"));
	}
	
	@PutMapping("/appointments/{id}/status")
	public ApiResponse<?> updateStatus(@PathVariable Long id, @RequestParam String status, HttpServletRequest req) {
		return appointmentService.updateStatus(id, status, (String) req.getAttribute("username"));
	}
}
