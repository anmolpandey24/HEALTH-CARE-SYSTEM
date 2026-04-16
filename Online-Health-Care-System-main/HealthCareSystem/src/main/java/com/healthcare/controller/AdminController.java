package com.healthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.dto.ApiResponse;
import com.healthcare.services.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")


@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")

public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	
	@GetMapping("/centers")
	public ApiResponse<?> getAllCenters() {
		return adminService.getAllCenter();
	}
	
	@GetMapping("/centers/pending")
	public ApiResponse<?> getPendingCenters() {
		return adminService.getPendingCenters();
	}
	
	@PutMapping("/centers/{id}/approve")
	public ApiResponse<?> approveCenter(@PathVariable Long id) {
		return adminService.approveCenter(id);
	}
	@PutMapping("/centers/{id}/reject")
	public ApiResponse<?> rejectCenter(@PathVariable Long id) {
		return adminService.rejectCenter(id);
	}
	
	@GetMapping("/users")
	public ApiResponse<?> getAllUsers(){
		return adminService.getAllUsers();
	}
	
	@PutMapping("/users/{id}/block")
	public ApiResponse<?> block(@PathVariable Long id) {
		return adminService.blockUser(id);
	}
	
	@PutMapping("/users/{id}/unblock")
	public ApiResponse<?> unblock(@PathVariable Long id) {
		return adminService.unblockUser(id);
	}
	
}
