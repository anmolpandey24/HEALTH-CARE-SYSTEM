package com.healthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.CenterTestRequest;
import com.healthcare.dto.DiagnosticTestRequest;
import com.healthcare.services.DiagnosticTestService;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor


@CrossOrigin(origins = "http://localhost:4200")
public class DiagnosticTestController {

	@Autowired
	DiagnosticTestService service;
	
	@PostMapping
	public ApiResponse<?> create(@RequestBody DiagnosticTestRequest request) {
		return service.createTest(request);
	}
	
	@GetMapping
	public ApiResponse<?> getAll() {
		return service.getAllTests();
	}
	
	@PostMapping("/center/{centerId}") 
	public ApiResponse<?> addToCenter(@PathVariable Long centerId, @RequestBody CenterTestRequest request, HttpServletRequest http) {
		return service.addTestToCenter(centerId, request, (String) http.getAttribute("username"));
	}
	
	@GetMapping("/center/{centerId}")
	public ApiResponse<?> getByCenter(@PathVariable Long centerId) {
		return service.getTestsByCenter(centerId);
		
	}
}
