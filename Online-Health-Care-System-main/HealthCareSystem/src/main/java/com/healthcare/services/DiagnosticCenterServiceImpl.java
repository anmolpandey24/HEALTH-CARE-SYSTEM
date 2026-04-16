package com.healthcare.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.CenterRequest;
import com.healthcare.dto.CenterTestRequest;
import com.healthcare.entities.CenterTest;
import com.healthcare.entities.DiagnosticCenter;
import com.healthcare.entities.DiagnosticTest;
import com.healthcare.entities.User;
import com.healthcare.exceptions.AdminAccessException;
import com.healthcare.exceptions.BadRequestException;
import com.healthcare.mapper.CenterMapper;
import com.healthcare.repository.CenterTestRepository;
import com.healthcare.repository.DiagnosticCenterRepository;
import com.healthcare.repository.DiagnosticTestRepository;
import com.healthcare.repository.UserRepository;

@Service
public class DiagnosticCenterServiceImpl implements DiagnosticCenterService{

	@Autowired
	DiagnosticCenterRepository centerRepo;
	@Autowired
	DiagnosticTestRepository testRepo;
	
	@Autowired
	UserRepository userRepo;
	@Autowired
	CenterTestRepository centerTestRepo;
	
	
	@Override
	public ApiResponse<?> requestCenter(CenterRequest request, String username) {
		User user = userRepo.findByUsername(username).orElseThrow();
		
		if(!user.getRole().equals("ADMIN")) {
			throw new AdminAccessException("Only Admin can create Center");
		}
		
		DiagnosticCenter center = CenterMapper.toEntity(request);
		if (center.getOwnerName() == null || center.getOwnerName().isBlank()) {
			center.setOwnerName(username);
		}
		center.setUser(user);
		center.setStatus("PENDING");
		centerRepo.save(center);
		
		return new ApiResponse<>(true, "Center requested", null);
	}

	@Override
	public ApiResponse<?> getApprovedCenters() {
		return new ApiResponse<>(true, "Approved Centers", centerRepo.findByStatus("APPROVED"));
	}

	@Override
	public ApiResponse<?> getMyCenter(String username) {
		User user = userRepo.findByUsername(username).orElseThrow();
		DiagnosticCenter center = centerRepo.findByUserId(user.getId());
		return new ApiResponse<>(true, "My Center", center);
	}

	@Override
	public ApiResponse<?> addTest(Long centerId, CenterTestRequest request, String username) {
		
		DiagnosticCenter center = centerRepo.findById(centerId).orElseThrow();
		
		if(!center.getUser().getUsername().equals(username)) {
			throw new AdminAccessException("Access Denied");
			
			
		}
		
		DiagnosticTest test = testRepo.findById(request.getTestId()).orElseThrow();
		//center.getTests().add(test);
		
		CenterTest ct = new CenterTest();
		ct.setCenter(center);
		ct.setTest(test);
		ct.setMaxBooking(request.getMaxBooking());
		//centerRepo.save(center);
		centerTestRepo.save(ct);
		
		return new ApiResponse<>(true, "Test added", null);
	}

	@Override
	public ApiResponse<?> removeTest(Long centerId, Long testId, String username) {
DiagnosticCenter center = centerRepo.findById(centerId).orElseThrow();
		
		if(!center.getUser().getUsername().equals(username)) {
			throw new BadRequestException("Access Denied");
		}
		
		DiagnosticTest test = testRepo.findById(testId).orElseThrow();
		CenterTest ct = centerTestRepo.findByCenterIdAndTestId(centerId, testId).stream().findFirst().orElse(null);
		centerTestRepo.delete(ct);
		
		return new ApiResponse<>(true, "Test removed", null);
	}

	@Override
	public ApiResponse<?> getCenterTests(Long centerId) {
		//DiagnosticCenter center = centerTestRepo.findByCenterId(centerId).orElseThrow();
		List<CenterTest> list = centerTestRepo.findByCenterId(centerId);
  return new ApiResponse<>(true, "Tests", list);
		
	}

	
}
