package com.healthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.healthcare.dto.ApiResponse;
import com.healthcare.entities.Report;
import com.healthcare.services.ReportService;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

	@Autowired
	ReportService service;
	
	@PostMapping("/upload/{appointmentId}")
	public ApiResponse<?> upload(@PathVariable Long appointmentId, @RequestParam String patientName, @RequestParam("file") MultipartFile file, HttpServletRequest req) {
		return service.upload(appointmentId, patientName, file, (String) req.getAttribute("username"));
	}
	
	@GetMapping 
	public ApiResponse<?> get(@RequestParam String patientName) {
		return service.getReports(patientName);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downlaod(@PathVariable Long id) {
		Report report = service.getById(id);
		System.out.println("filename" + report.getFileName());
		System.out.println("data length" + (report.getData() !=null ? report.getData().length :"NULL"));
		return ResponseEntity.ok()
				.header("Content-Type", report.getFileType() != null ? report.getFileType() : "application/octet-stream")
				.header("Content-Disposition", "attachment; filename=\"" +report.getFileName() + "\"")
				.body(report.getData());
	}
}
