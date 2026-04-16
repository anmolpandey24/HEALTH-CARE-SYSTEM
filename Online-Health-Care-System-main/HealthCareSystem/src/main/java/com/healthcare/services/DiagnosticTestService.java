package com.healthcare.services;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.CenterTestRequest;
import com.healthcare.dto.DiagnosticTestRequest;

public interface DiagnosticTestService {

	ApiResponse<?> createTest(DiagnosticTestRequest request);
	ApiResponse<?> getAllTests();
	
	ApiResponse<?> addTestToCenter(Long centerId, CenterTestRequest request, String username);
	ApiResponse<?> getTestsByCenter(Long centerId);
}
