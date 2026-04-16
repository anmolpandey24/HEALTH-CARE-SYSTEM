package com.healthcare.services;

import com.healthcare.repository.DiagnosticCenterRepository;
import com.healthcare.repository.ReportRepository;
import com.healthcare.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.healthcare.dto.ApiResponse;
import com.healthcare.entities.DiagnosticCenter;
import com.healthcare.entities.User;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

	@Autowired
	DiagnosticCenterRepository centerRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ReportRepository reportRepo;
	

	@Override
	public ApiResponse<?> approveCenter(Long centerId) {
		DiagnosticCenter center = centerRepo.findById(centerId).orElseThrow(() -> new RuntimeException("Center not found"));
		center.setStatus("APPROVED");
		centerRepo.save(center);
		return new ApiResponse<>(true, "Center Approved", center);
		
	}
	
	@Override
	public ApiResponse<?> rejectCenter(Long centerId) {
		DiagnosticCenter center = centerRepo.findById(centerId).orElseThrow(() -> new RuntimeException("Center not found"));
		center.setStatus("REJECTED");
		centerRepo.save(center);
		return new ApiResponse<>(true, "Center Rejected", center);
	}
	
	@Override
	public ApiResponse<?> getAllCenter() {
		
		return new ApiResponse<>(true, "All Centers", centerRepo.findAll());
	}
	
	
	@Override
	public ApiResponse<?> getAllUsers() {
		
		return new ApiResponse<>(true, "All Users", userRepo.findAll() );
	}

	@Override
	public ApiResponse<?> getAllReports() {
		return new ApiResponse<>(true, "All Reports", reportRepo.findAll());
	}

	@Override
	public ApiResponse<?> getPendingCenters() {
		
		List<DiagnosticCenter> list = centerRepo.findByStatus("PENDING");
		return new ApiResponse<>(true, "Pending Centers", list);
	}

	@Override
	public ApiResponse<?> blockUser(Long userId) {
		User user = userRepo.findById(userId).orElse(null);
		if(user==null) {
			return new ApiResponse<>(false, "User not found", null);
		}
		
		user.setActive(false);
		userRepo.save(user);
		return new ApiResponse<>(true, "User Blocked Successully", userId);
	}

	@Override
	public ApiResponse<?> unblockUser(Long userId) {
		User user = userRepo.findById(userId).orElse(null);
		if(user==null) {
			return new ApiResponse<>(false, "User not found", null);
		}
		
		user.setActive(true);
		userRepo.save(user);
		return new ApiResponse<>(true, "User UnBlocked Successully", userId);
	}
	
	
}
