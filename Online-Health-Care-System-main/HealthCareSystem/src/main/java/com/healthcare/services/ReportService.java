package com.healthcare.services;

import org.springframework.web.multipart.MultipartFile;

import com.healthcare.dto.ApiResponse;
import com.healthcare.entities.Report;

public interface ReportService {

	ApiResponse<?> upload(Long centerId, String patientName, MultipartFile file, String username);
	ApiResponse<?> getReports(String patientName);
	Report getById(Long id);
}
