package com.healthcare.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.CenterTestRequest;
import com.healthcare.dto.DiagnosticTestRequest;
import com.healthcare.entities.CenterTest;
import com.healthcare.entities.DiagnosticCenter;
import com.healthcare.entities.DiagnosticTest;
import com.healthcare.repository.CenterTestRepository;
import com.healthcare.repository.DiagnosticCenterRepository;
import com.healthcare.repository.DiagnosticTestRepository;

@Service
public class DiagnosticTestServiceImpl implements DiagnosticTestService {

	@Autowired
	DiagnosticTestRepository testRepo;
	
	@Autowired
	CenterTestRepository centerTestRepo;
	
	@Autowired
	DiagnosticCenterRepository centerRepo;
	
	@Override
	public ApiResponse<?> createTest(DiagnosticTestRequest request) {
		
		DiagnosticTest test = new DiagnosticTest();
		test.setTestname(request.getTestName());
		test.setDescription(request.getDescription());
		test.setPrice(request.getPrice());
		testRepo.save(test);
		return new ApiResponse<>(true, "Test Created", test);
	}

	@Override
	public ApiResponse<?> getAllTests() {
		return new ApiResponse<>(true, "All Tests", testRepo.findAll());
		
	}

	@Override
	public ApiResponse<?> addTestToCenter(Long centerId, CenterTestRequest request, String username) {
		DiagnosticCenter center = centerRepo.findById(centerId).orElseThrow(() -> new RuntimeException("Center not Found"));
		if(! center.getUser().getUsername().equals(username)) {
			return new ApiResponse<>(false, "Access Denied ",null);
		}
		if(!center.getStatus().equals("APPROVED")){
			return new ApiResponse<>(false, "Access Denied ", center.getOwnerName());
		}
		DiagnosticTest test = testRepo.findById(request.getTestId()).orElseThrow(() -> new RuntimeException("Test Not Found"));
		
		// Check if CenterTest already exists
		CenterTest existingCt = centerTestRepo.findByCenterIdAndTestId(centerId, request.getTestId()).stream().findFirst().orElse(null);
		if (existingCt != null) {
			// Update existing entry
			existingCt.setMaxBooking(request.getMaxBooking());
			centerTestRepo.save(existingCt);
			return new ApiResponse<>(true, "Test updated in Center", centerId);
		} else {
			// Create new entry
			CenterTest ct = new CenterTest();
			ct.setCenter(center);
			ct.setTest(test);
			ct.setMaxBooking(request.getMaxBooking());
			centerTestRepo.save(ct);
			return new ApiResponse<>(true, "Test added to Center", centerId);
		}
	}

	@Override
	public ApiResponse<?> getTestsByCenter(Long centerId) {
		
		List<CenterTest> list = centerTestRepo.findByCenterId(centerId);
		return new ApiResponse<>(true, "Center Tests", list);
	}

}
