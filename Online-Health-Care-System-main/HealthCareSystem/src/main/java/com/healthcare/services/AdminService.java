package com.healthcare.services;

import com.healthcare.dto.ApiResponse;

public interface AdminService {

	ApiResponse<?> approveCenter(Long centerId);
	ApiResponse<?> getPendingCenters();
	ApiResponse<?> rejectCenter(Long centerId);
	ApiResponse<?> getAllCenter();
	ApiResponse<?> getAllUsers();
	ApiResponse<?> getAllReports();
	ApiResponse<?> blockUser(Long userId);
	ApiResponse<?> unblockUser(Long userId);

}
