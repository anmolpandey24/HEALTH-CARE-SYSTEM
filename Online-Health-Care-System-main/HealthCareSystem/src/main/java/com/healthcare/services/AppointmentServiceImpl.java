package com.healthcare.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.AppointmentRequest;
import com.healthcare.dto.AppointmentResponse;
import com.healthcare.entities.Appointment;
import com.healthcare.entities.AppointmentStatus;
import com.healthcare.entities.CenterTest;
import com.healthcare.entities.DiagnosticCenter;
import com.healthcare.entities.DiagnosticTest;
import com.healthcare.entities.User;
import com.healthcare.exceptions.ApointmentNotFoundException;
import com.healthcare.exceptions.CenterNotFoundException;
import com.healthcare.exceptions.SlotFullException;
import com.healthcare.exceptions.TestNotAvailableException;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.repository.CenterTestRepository;
import com.healthcare.repository.DiagnosticCenterRepository;
import com.healthcare.repository.DiagnosticTestRepository;
import com.healthcare.repository.UserRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService{

	@Autowired
	AppointmentRepository appointmentRepo;
	@Autowired
	DiagnosticCenterRepository centerRepo;
	
	@Autowired
	CenterTestRepository centerTestRepo;
	
	@Autowired
	DiagnosticTestRepository testRepo;
	
	@Autowired
	UserRepository userRepo;

	@Override
	public ApiResponse<?> bookAppointment(AppointmentRequest request, String username) {
		
		User user = userRepo.findByUsername(username).orElse(null);
		
		if(user==null) {
			return new ApiResponse<>(false, "UserNot found", null);
		}
		DiagnosticCenter center = centerRepo.findById(request.getCenterId()).orElse(null);
		if(center == null) {
			throw new CenterNotFoundException("Center Not Found");
			
		}
		
		if(! center.getStatus().equals("APPROVED")) {
			return new ApiResponse<>(false, "Center is not approved yet", null);
		}
		
		DiagnosticTest test = testRepo.findById(request.getTestId()).orElse(null);
		if(test == null) {
			return new ApiResponse<>(false, "Test not Found",  null);
			
		}
		
		boolean exists = appointmentRepo.existsByUserIdAndCenterIdAndTestIdAndAppointmentDate(user.getId(), center.getId(), test.getId(), request.getAppointmentDate());
		
		if(exists) {
			return new ApiResponse<>(false, "You have already booked this test", null);
		}
		
		CenterTest ct = centerTestRepo.findByCenterIdAndTestId(request.getCenterId(), request.getTestId()).stream().findFirst().orElse(null);
		if(ct == null) {
			throw new TestNotAvailableException("Test Not Available");
			
		}
		if(request.getAppointmentDate().isBefore(LocalDate.now())) {
			return new ApiResponse<>(false, "Cannot book appointments past date", request.getCenterId());
		}
		
		boolean alreadyBooked = appointmentRepo.existsByUserAndTestAndAppointmentDateAndSlot(user, test, request.getAppointmentDate(), request.getSlot());
		
		
		long count = appointmentRepo.countByCenterIdAndTestIdAndAppointmentDateAndSlot(request.getCenterId(), request.getTestId(), request.getAppointmentDate(), request.getSlot());
		if(count>=ct.getMaxBooking()) {
			throw new SlotFullException("Slot is Fulll !!");
		}
		Appointment ap = new Appointment();
		ap.setUser(user);
		ap.setCenter(center);
		ap.setTest(test);
		ap.setAppointmentDate(request.getAppointmentDate());
		ap.setSlot(request.getSlot());
		ap.setStatus(AppointmentStatus.PENDING);
		appointmentRepo.save(ap);
		return new ApiResponse<>(true, "Appointment Booked (Pending)", ap);
	}

	@Override
	public ApiResponse<?> getMyAppointments(String username) {
		User user = userRepo.findByUsername(username).orElse(null);
		if(user==null) {
			return new ApiResponse<>(false, "UserNot found", null);
		}
		
		List<Appointment> appointments = appointmentRepo.findByUserId(user.getId());
		List<AppointmentResponse> responseList = new ArrayList<>();
		
		for(Appointment ap : appointments) {
			AppointmentResponse response = new AppointmentResponse();
			response.setId(ap.getId());
			response.setUserId(ap.getUser().getId());
			response.setCenterId(ap.getCenter().getId());
			response.setTestId(ap.getTest().getId());
			response.setStatus(ap.getStatus().toString());
			response.setAppointmentDate(ap.getAppointmentDate().toString());
			response.setAppointmentTime(ap.getSlot().toString());
			response.setUserName(ap.getUser().getUsername());
			response.setCenterName(ap.getCenter().getCenterName());
			response.setTestName(ap.getTest().getTestname());
			response.setTestPrice(ap.getTest().getPrice().intValue());
			responseList.add(response);
		}
		
		return new ApiResponse<>(true, "My Appointments", responseList);
	}

	@Override
	public ApiResponse<?> cancelAppointment(Long id, String username) {
		
		User user = userRepo.findByUsername(username).orElse(null);
		if(user==null) {
			return new ApiResponse<>(false, "User Not found", null);
		}
		
		Appointment ap= appointmentRepo.findById(id).orElse(null);
		if(ap == null) {
			return new ApiResponse<>(false, "Appointment not found", null);
		}
		
		if(ap.getUser().getId()!=user.getId()) {
			return new ApiResponse<>(false, "Access Denied", username);
			
		}
		
		// If appointment was CONFIRMED, restore the maxBooking count
		if (ap.getStatus().equals(AppointmentStatus.CONFIRMED)) {
			CenterTest centerTest = centerTestRepo.findByCenterIdAndTestId(ap.getCenter().getId(), ap.getTest().getId()).stream().findFirst().orElse(null);
			if (centerTest != null) {
				centerTest.setMaxBooking(centerTest.getMaxBooking() + 1);
				centerTestRepo.save(centerTest);
			}
		}
		
		appointmentRepo.delete(ap);
		
		return new ApiResponse<>(true, "Appointment Cancelled", username);
	}

	@Override
	public ApiResponse<?> updateStatus(Long id, String status, String username) {
		User user = userRepo.findByUsername(username).orElse(null);
		if(user == null) {
			return new ApiResponse<>(false, "User not found", null);
		}
		
		Appointment ap = appointmentRepo.findById(id).orElse(null);
		if(ap == null) {
			throw new ApointmentNotFoundException("Appointment not Found");
		}
		
		// Check if the user owns the center for this appointment
		DiagnosticCenter center = centerRepo.findByUserId(user.getId());
		if(center == null || !center.getId().equals(ap.getCenter().getId())) {
			return new ApiResponse<>(false, "Access Denied", null);
		}
		
		String normalizedStatus = status == null ? "" : status.trim().toUpperCase();
		if(normalizedStatus.isEmpty()) {
			return new ApiResponse<>(false, "Invalid status", null);
		}
		
		AppointmentStatus oldStatus = ap.getStatus();
		
		try {
			ap.setStatus(AppointmentStatus.valueOf(normalizedStatus));
		} catch (IllegalArgumentException e) {
			return new ApiResponse<>(false, "Invalid status value: " + status, null);
		}
		
		// Handle maxBooking update when status changes to CONFIRMED
		if (normalizedStatus.equals("CONFIRMED") && !oldStatus.equals(AppointmentStatus.CONFIRMED)) {
			CenterTest centerTest = centerTestRepo.findByCenterIdAndTestId(ap.getCenter().getId(), ap.getTest().getId()).stream().findFirst().orElse(null);
			if (centerTest != null && centerTest.getMaxBooking() > 0) {
				centerTest.setMaxBooking(centerTest.getMaxBooking() - 1);
				centerTestRepo.save(centerTest);
			}
		}
		// Handle maxBooking update when status changes from CONFIRMED to another status (e.g., CANCELLED)
		else if (!normalizedStatus.equals("CONFIRMED") && oldStatus.equals(AppointmentStatus.CONFIRMED)) {
			CenterTest centerTest = centerTestRepo.findByCenterIdAndTestId(ap.getCenter().getId(), ap.getTest().getId()).stream().findFirst().orElse(null);
			if (centerTest != null) {
				centerTest.setMaxBooking(centerTest.getMaxBooking() + 1);
				centerTestRepo.save(centerTest);
			}
		}
		
		appointmentRepo.save(ap);
		
		return new ApiResponse<>(true, "Status Updated", ap);
	}

	@Override
	public ApiResponse<?> getCenterAppointments(String username) {
		User user = userRepo.findByUsername(username).orElse(null);
		if(user==null) {
			return new ApiResponse<>(false, "User Not found", null);
		}
		
		DiagnosticCenter center = centerRepo.findByUserId(user.getId());
		if(center == null ){
			return new ApiResponse<>(false, "No Center", null);
		}
		
		List<Appointment> appointments = appointmentRepo.findByCenterId(center.getId());
		List<AppointmentResponse> responseList = new ArrayList<>();
		
		for(Appointment ap : appointments) {
			AppointmentResponse response = new AppointmentResponse();
			response.setId(ap.getId());
			response.setUserId(ap.getUser().getId());
			response.setCenterId(ap.getCenter().getId());
			response.setTestId(ap.getTest().getId());
			response.setStatus(ap.getStatus().toString());
			response.setAppointmentDate(ap.getAppointmentDate().toString());
			response.setAppointmentTime(ap.getSlot().toString());
			response.setUserName(ap.getUser().getUsername());
			response.setCenterName(ap.getCenter().getCenterName());
			response.setTestName(ap.getTest().getTestname());
			response.setTestPrice(ap.getTest().getPrice().intValue());
			responseList.add(response);
		}
		
		return new ApiResponse<>(true, "Appointments", responseList);
	}
	
	
}

