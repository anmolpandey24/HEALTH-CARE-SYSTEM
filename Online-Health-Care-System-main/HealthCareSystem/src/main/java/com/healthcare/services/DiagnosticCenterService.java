package com.healthcare.services;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.CenterRequest;
import com.healthcare.dto.CenterTestRequest;

public interface DiagnosticCenterService {
	
	ApiResponse<?> requestCenter(CenterRequest request, String username);
	ApiResponse<?> getApprovedCenters();
	ApiResponse<?> getMyCenter(String username);
	ApiResponse<?> addTest(Long centerId, CenterTestRequest request, String username);
	ApiResponse<?> removeTest(Long centerId, Long testId, String username);
	ApiResponse<?> getCenterTests(Long centerId);


	
	
	

}
