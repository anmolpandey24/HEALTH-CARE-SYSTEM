package com.healthcare.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.healthcare.dto.ApiResponse;
import com.healthcare.entities.Appointment;
import com.healthcare.entities.DiagnosticCenter;
import com.healthcare.entities.Report;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.repository.DiagnosticCenterRepository;
import com.healthcare.repository.ReportRepository;

import jakarta.validation.ReportAsSingleViolation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

	@Autowired
	ReportRepository repo;
	@Autowired
	DiagnosticCenterRepository centerRepo;
	@Autowired
	AppointmentRepository appointmentRepo;
	
	@Override
	public ApiResponse<?> upload(Long appointmentId, String patientName, MultipartFile file, String username) {
		Appointment appointment = appointmentRepo.findById(appointmentId).orElse(null);
		
		try {
			if(appointment == null) {
				return new ApiResponse<>(false, "Appointment not found", null);
			}
			
			DiagnosticCenter center = appointment.getCenter();
			
			if(center == null) {
				return new ApiResponse<>(false, "Center not found for appointment", null);
			}
			
			if(!center.getUser().getUsername().equals(username)) {
				return new ApiResponse<>(false, "Access Denied", null);
			}
			
			if(!center.getStatus().equals("APPROVED")) {
				return new ApiResponse<>(false, "Center Not Approved Yet", null);
			}
			
			if(file.isEmpty()) {
				return new ApiResponse<>(false, "File is empty", null);
			}
			
			Report r = new Report();
			r.setPatientName(patientName);
			r.setFileName(file.getOriginalFilename());
			r.setFileType(file.getContentType());
			r.setData(file.getBytes());
			r.setAppointment(appointment);
			repo.save(r);
			return new ApiResponse<>(true, "Report Uploaded Successfully", r);
			
		}catch (Exception e) {
			e.printStackTrace();
			return new ApiResponse<>(false, "Upload failed: " + e.getMessage(), null);
		}
	}
	@Override
	public ApiResponse<?> getReports(String patientName) {
		return new ApiResponse<>(true, "Reports", repo.findByPatientName(patientName));
	}
	@Override
	public Report getById(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("Report Not found"));
	}
	
	
}
